# Robotics Section - Managing Content

This directory contains the FTC Robotics team pages for the website.

## How to Add Content

### For Students/Team Members

1. **Write in Markdown**: Create or edit `.md` files with your content
2. **Send to Mr. Reveal**: Email your markdown file or text to nreveal@mnps.org
3. **He'll update the site**: Mr. Reveal will add it to the website repository

### For Mr. Reveal

#### Adding/Editing Pages

All robotics pages are in `src/robotics/`. Each `.md` file becomes a page:

- `team.md` → `/robotics/team/`
- `documentation.md` → `/robotics/documentation/`
- `resources.md` → `/robotics/resources/`
- etc.

**To add a new page:**

1. Create a new `.md` file in `src/robotics/`
2. Add frontmatter:
```markdown
---
title: Page Title
permalink: /robotics/page-name/index.html
description: 'Brief description for SEO'
---

Your content here...
```

3. Add a link to it from the main robotics landing page (`src/pages/robotics.md`)

#### Adding PDFs

PDFs are stored in `src/assets/pdfs/` and automatically copied to the output.

**To add a PDF:**

1. Place the PDF in `src/assets/pdfs/`
   - Example: `src/assets/pdfs/robot-assembly-guide.pdf`

2. Link to it from any markdown file:
```markdown
[Robot Assembly Guide](/assets/pdfs/robot-assembly-guide.pdf)
```

3. Good practice: Add description and metadata
```markdown
### Build Guides
- [Robot Assembly Guide](/assets/pdfs/robot-assembly-guide.pdf) - Complete step-by-step assembly instructions (2.3 MB)
- [Wiring Diagram](/assets/pdfs/wiring-diagram.pdf) - Electrical system layout (850 KB)
```

**PDF Best Practices:**
- Use descriptive filenames (no spaces, use hyphens)
- Keep file sizes reasonable (compress if needed)
- Include version/date in filename if it will be updated
  - Example: `robot-assembly-v2-2024.pdf`

#### Navigation

The main "Robotics" link in the top navigation is managed in:
- `src/_data/navigation.js`

It's already set up! The robotics section will appear in the main menu.

#### The robotics.json File

The `robotics.json` file sets default data for all markdown files in this directory:

```json
{
  "layout": "page",
  "tags": "robotics"
}
```

This means all files in `src/robotics/` automatically:
- Use the "page" layout
- Get tagged as "robotics"

You don't need to repeat this in individual markdown files' frontmatter.

## Existing Pages

- `/robotics/` - Main landing page (in `src/pages/robotics.md`)
- `/robotics/team/` - Team information
- `/robotics/documentation/` - Technical documentation links
- `/robotics/resources/` - PDFs and build guides
- `/robotics/schedule/` - Meeting and competition schedule
- `/robotics/goals/` - Season goals and objectives
- `/robotics/programming/` - Programming resources and code

## Building and Deploying

The site auto-deploys to Netlify when you push to GitHub:

```bash
cd APCSA2025
git add .
git commit -m "Update robotics content"
git push
```

Netlify will automatically:
1. Run `npm run build`
2. Deploy the `dist/` folder
3. Your changes go live in ~2 minutes

## Testing Locally

To preview changes before pushing:

```bash
cd APCSA2025
npm start
```

This starts a local server at `http://localhost:8080`. The site will auto-refresh when you save changes.

## Need Help?

- [11ty Documentation](https://www.11ty.dev/docs/)
- [Markdown Guide](https://www.markdownguide.org/)
- This site's main docs: `/docs/` section of the website
