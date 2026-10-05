import { BrowserVerifier, prepareVerifier } from '../../verifier/client.mjs';

let preparationError;
try { await prepareVerifier(); } catch (error) { preparationError = error.message; }

(function () {
    const $ = (id) => document.getElementById(id);
    const root = $('ljGallery');
    if (!root) return;

    const verifier = new BrowserVerifier();
    let renderToken = 0;
    let files;
    const state = {
      examples: [],
      activeId: null,
      activeTab: 'spec',
      complexity: 'all',
      factors: new Set(),
      mermaidReady: false,
      runToken: 0,
    };

    const fileFor = (tab) => ({
      spec: 'spec.java',
      correct: 'correct.java',
      incorrect: 'incorrect.java',
    }[tab]);

    async function fetchText(path) {
      const r = await fetch(path, { cache: 'no-cache' });
      if (!r.ok) throw new Error(path + ' -> ' + r.status);
      return r.text();
    }

    function passesFilter(ex) {
      if (state.complexity !== 'all' && ex.complexity !== state.complexity) return false;
      for (const f of state.factors) if (!ex.factors.includes(f)) return false;
      return true;
    }

    function renderList() {
      const list = $('ljList');
      list.innerHTML = '';
      const visible = state.examples.filter(passesFilter);
      $('ljCount').textContent =
        visible.length + ' of ' + state.examples.length + ' examples';
      for (const ex of visible) {
        const li = document.createElement('li');
        li.className = 'lj-item' + (ex.id === state.activeId ? ' lj-item--active' : '');
        li.dataset.id = ex.id;
        li.innerHTML =
          '<span class="lj-item-title">' + ex.title + '</span>' +
          '<span class="lj-item-class">' + ex.className + '</span>' +
          '<span class="lj-badge lj-badge--' + ex.complexity + '">' + ex.complexity + '</span>';
        li.addEventListener('click', () => selectExample(ex.id));
        list.appendChild(li);
      }
      if (visible.length && !visible.some(e => e.id === state.activeId)) {
        selectExample(visible[0].id);
      }
    }

    function resetVerification() {
      state.runToken++;
      if (verifier.pending) verifier.cancel();
      files = undefined;
      $('ljRun').disabled = true;
      $('ljRun').textContent = '▶ Run verifier';
      $('ljVerifier').dataset.state = 'spec';
      $('ljVerifierBody').textContent = 'Loading example…';
    }

    function showResult(result) {
      $('ljVerifier').dataset.state = result.status === 'success' ? 'ok' : 'err';
      const body = $('ljVerifierBody');
      body.replaceChildren();
      for (const diagnostic of result.diagnostics) {
        const paragraph = document.createElement('p');
        const title = document.createElement('strong');
        title.textContent = diagnostic.title;
        paragraph.append(title, document.createElement('br'), document.createTextNode(diagnostic.message));
        body.append(paragraph);
      }
    }

    async function renderDetail() {
      const ex = state.examples.find(e => e.id === state.activeId);
      if (!ex) return;
      $('ljClass').textContent = ex.className;
      $('ljTitle').textContent = ex.title;
      $('ljBlurb').textContent = ex.blurb;
      $('ljMeta').innerHTML =
        '<span class="lj-badge lj-badge--' + ex.complexity + '">' + ex.complexity + '</span>' +
        ex.factors.map(f => '<span class="lj-factor">' + f + '</span>').join('');
      renderTab();
      const host = $('ljMermaid');
      host.replaceChildren();
      try {
        const mer = await fetchText('assets/examples/data/' + ex.id + '/state.mmd');
        if (ex.id !== state.activeId) return;
        if (state.mermaidReady && window.mermaid) {
          const { svg } = await window.mermaid.render('ljMermaidGraph_' + Date.now(), mer);
          if (ex.id === state.activeId) host.innerHTML = svg;
        } else host.textContent = mer;
      } catch {
        if (ex.id === state.activeId) host.textContent = 'Diagram unavailable.';
      }
    }

    async function renderTab() {
      const token = ++renderToken;
      resetVerification();
      const ex = state.examples.find(e => e.id === state.activeId);
      const tab = state.activeTab;
      try {
        const [spec, code] = await Promise.all([
          fetchText('assets/examples/data/' + ex.id + '/spec.java'),
          fetchText('assets/examples/data/' + ex.id + '/' + fileFor(tab))
        ]);
        if (token !== renderToken) return;
        const codeEl = $('ljCode');
        codeEl.className = 'language-java';
        codeEl.textContent = code;
        window.Prism?.highlightElement(codeEl);
        const specName = spec.match(/(?:class|interface)\s+(\w+)/)[1] + '.java';
        files = { [specName]: spec };
        if (tab !== 'spec') files['Example.java'] = code;
        $('ljRun').disabled = false;
        $('ljVerifierBody').textContent = tab === 'spec'
          ? 'Check this specification, or select a usage to verify its calls.'
          : 'Run the verifier to check this usage against its specification.';
      } catch (error) {
        if (token !== renderToken) return;
        showResult({ status: 'failure', diagnostics: [{ title: 'Example could not load', message: error.message }] });
      }
    }

    async function runVerifier() {
      if (verifier.pending) {
        state.runToken++;
        verifier.cancel();
        $('ljRun').textContent = '▶ Run verifier';
        $('ljVerifier').dataset.state = 'spec';
        $('ljVerifierBody').textContent = 'Verification stopped.';
        return;
      }
      if (!files) return;
      const token = ++state.runToken;
      $('ljRun').textContent = '■ Stop';
      $('ljVerifier').dataset.state = 'spec';
      try {
        if (preparationError) throw new Error(preparationError);
        const result = await verifier.verify(files, message => {
          if (token === state.runToken) $('ljVerifierBody').textContent = message;
        });
        if (token === state.runToken) showResult(result);
      } catch (error) {
        if (token === state.runToken && error.name !== 'AbortError') {
          showResult({ status: 'failure', diagnostics: [{ title: 'Verification could not complete', message: error.message }] });
        }
      } finally {
        if (token === state.runToken) $('ljRun').textContent = '▶ Run verifier';
      }
    }

    function selectExample(id) {
      state.activeId = id;
      // update list active state
      for (const li of document.querySelectorAll('#ljList .lj-item')) {
        li.classList.toggle('lj-item--active', li.dataset.id === id);
      }
      renderDetail();
    }

    function selectTab(tab) {
      state.activeTab = tab;
      for (const btn of document.querySelectorAll('.lj-tab')) {
        const isActive = btn.dataset.tab === tab;
        btn.classList.toggle('lj-tab--active', isActive);
        btn.setAttribute('aria-selected', isActive ? 'true' : 'false');
      }
      renderTab();
    }

    function wireFilters() {
      for (const group of document.querySelectorAll('.lj-filters')) {
        const kind = group.dataset.group;
        for (const chip of group.querySelectorAll('.lj-chip')) {
          chip.addEventListener('click', () => {
            const v = chip.dataset.value;
            if (kind === 'complexity') {
              state.complexity = v;
              for (const c of group.querySelectorAll('.lj-chip'))
                c.classList.toggle('lj-chip--active', c === chip);
            } else {
              if (state.factors.has(v)) state.factors.delete(v);
              else state.factors.add(v);
              chip.classList.toggle('lj-chip--active', state.factors.has(v));
            }
            renderList();
          });
        }
      }
    }

    function wireTabs() {
      for (const btn of document.querySelectorAll('.lj-tab')) {
        btn.addEventListener('click', () => selectTab(btn.dataset.tab));
      }
      $('ljRun').addEventListener('click', runVerifier);
    }

    async function init() {
      try {
        state.examples = await (await fetch('assets/examples/examples.json', { cache: 'no-cache' })).json();
      } catch (e) {
        root.innerHTML = '<p class="lj-error">Could not load examples.</p>';
        return;
      }
      if (window.mermaid) {
        window.mermaid.initialize({
          startOnLoad: false,
          theme: 'base',
          themeVariables: {
            primaryColor: '#ffe7ee',
            primaryTextColor: '#212529',
            primaryBorderColor: '#cf0e4e',
            lineColor: '#cf0e4e',
            fontFamily: 'Nunito, system-ui, sans-serif',
            fontSize: '14px',
          },
          flowchart: { htmlLabels: true },
        });
        state.mermaidReady = true;
      } else {
        // mermaid loads with `defer`; poll briefly
        const start = Date.now();
        while (!window.mermaid && Date.now() - start < 3000) {
          await new Promise(r => setTimeout(r, 50));
        }
        if (window.mermaid) {
          window.mermaid.initialize({
            startOnLoad: false,
            theme: 'base',
            themeVariables: {
              primaryColor: '#fff7ec',
              primaryTextColor: '#1a1a2e',
              primaryBorderColor: '#1a1a2e',
              lineColor: '#1a1a2e',
              fontFamily: 'JetBrains Mono, monospace',
              fontSize: '14px',
            },
          });
          state.mermaidReady = true;
        }
      }
      wireFilters();
      wireTabs();
      state.activeId = state.examples[0].id;
      renderList();
      renderDetail();
    }

    if (document.readyState === 'loading') {
      document.addEventListener('DOMContentLoaded', init);
    } else {
      init();
    }
  })();
