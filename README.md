# liquid-java.github.io

Source for the [LiquidJava](https://github.com/liquid-java) project website, served via GitHub Pages from `main`.

## Local preview

Build the shared verifier in the adjacent docs checkout with JDK 17, then export it here:

```sh
cd ../liquidjava-docs
npm ci
npm run build:playground
node scripts/playground/export.mjs ../liquid-java.github.io
node node_modules/http-server/bin/http-server ../liquid-java.github.io -p 8001 -c-1
```

Open `http://localhost:8001`. Use HTTPS or localhost and a server with HTTP range support for the JARs. The isolation service worker reloads once; the verifier itself loads on the first Run. Each usage is checked together with its displayed specification, locally in a worker. Results contain only diagnostic titles and messages. Stop, changing examples, and changing tabs cancel pending verification.

## Structure

- `index.html` — single-page site (markup, content, and inline JS for navbar + theme toggle).
- `assets/css/style-starter.css` — vendored W3layouts template stylesheet plus LiquidJava overrides. Theme rules layered via `.light-theme` / `.dark-theme` on `<body>`.
- `assets/js/gallery.js` — gallery selection and browser verification.
- `assets/js/jquery-3.3.1.min.js` — vendored jQuery.
- `assets/images/`, `assets/docs/`, `assets/extension/` — static assets (images, poster PDF, VSCode `.vsix`).

## Theme toggle

Dark/light mode is toggled by inline JS at the bottom of `index.html`. The selected theme is persisted in `localStorage` under the `theme` key and applied as a class on `<body>` before `DOMContentLoaded` to avoid a flash of unstyled content. When adding new sections, add explicit color rules for both themes in `style-starter.css`.

## Deploying

Select **GitHub Actions** as the Pages build source. The Pages workflow builds the runtime from a pinned commit of `liquid-java/liquidjava-docs`, validates it, and publishes the static site on pushes to `main`. Update the checkout `ref` after testing a shared-runtime upgrade. Generated `verifier/` and `isolation.js` files are ignored by Git.

## Related repositories

- [`liquidjava`](https://github.com/liquid-java/liquidjava) — verifier, API, examples
- [`liquidjava-docs`](https://github.com/liquid-java/liquidjava-docs) — documentation
- [`liquidjava-tutorial`](https://github.com/liquid-java/liquidjava-tutorial) — tutorial
- [`vscode-liquidjava`](https://github.com/liquid-java/vscode-liquidjava) — VSCode extension

The introduction video opens on YouTube because the cross-origin isolation required by the verifier prevents embedding its player. Neighboring GitHub Pages projects are excluded from the root isolation worker’s navigation responses.
