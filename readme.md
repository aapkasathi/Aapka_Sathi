# AapkaSathi

AapkaSathi is a web-based project that combines **frontend, backend, and cloud-hosted databases** to deliver dynamic and interactive functionality. The project uses modern technologies like **HTML5, CSS3, JavaScript (ES6+), PHP 8.2**, and **Supabase (PostgreSQL 15)** for authentication and data storage.

---

## 🚀 Tech Stack

| Technology | Purpose | Version |
|------------|---------|---------|
| **HTML5** | Structure of the web pages | Latest supported |
| **CSS3** | Styling the web pages | Latest supported |
| **JavaScript (ES6 or later)** | Adds interactivity to pages | ECMAScript 2021+ |
| **PHP** | Backend logic, request processing | 8.2+ |
| **Supabase** | Cloud-hosted PostgreSQL, APIs, authentication | Latest stable |
| **PostgreSQL** | Database for structured data | 15 |

---

## 📂 Project Structure

```
project/
│-- index.html        # Frontend entry point
│-- style.css         # Styling
│-- script.js         # Client-side interactivity
│-- server.php        # Backend logic (PHP)
│-- /assets           # Images, icons, etc.
```

---

## 🛠️ How to Run Locally

Follow the steps below to set up and run the project on your local machine:

### Step 1 – Install Required Software
- **PHP (v8.2 or later)** → [Download PHP](https://www.php.net/downloads.php)  
  *(or install [XAMPP](https://www.apachefriends.org/index.html) for PHP + Apache server)*
- **VS Code (Editor)** → [Download VS Code](https://code.visualstudio.com/)
- *(Optional)* **Node.js & npm** → [Download Node.js](https://nodejs.org/en/download/)

---

### Step 2 – Clone the Repository
```bash
git clone https://github.com/aapkasathi.git
cd aapkasathi
```

---

### Step 3 – Set Up Project Files
Ensure the following core files exist:
- `index.html`
- `style.css`
- `script.js`
- `server.php`

---

### Step 4 – Install Dependencies
- **PHP dependencies (optional via Composer):**
```bash
composer init
```

- **Node.js tools (optional):**
```bash
npm init -y
npm install http-server
```

---

### Step 5 – Run the Project

#### ✅ Option 1: Frontend Only (Static Files)
```bash
npx http-server .
```
Visit → [http://localhost:8080](http://localhost:8080)

Or open `index.html` directly in your browser.

#### ✅ Option 2: Backend with PHP
- Using **PHP’s Built-in Server**:
```bash
php -S localhost:8000
```
Visit → [http://localhost:8000](http://localhost:8000)

- Using **XAMPP**:
  1. Place your project folder inside the `htdocs` directory.
  2. Start Apache from the XAMPP Control Panel.
  3. Visit → `http://localhost/aapkasathi/`

---

### Step 6 – Connect to Supabase
In your `server.php`, configure Supabase API:
```php
$SUPABASE_URL = "https://your-project.supabase.co";
$SUPABASE_KEY = "your-anon-or-service-key";
```
Use these credentials to make secure API calls for authentication, CRUD operations, and data handling.

---

## 🔒 Security Notes
- Keep **Supabase API keys** safe and never expose them on the frontend.
- Validate and sanitize all user inputs in PHP.
- Use environment variables (`.env`) for API credentials in production.

---

## 🌐 Core Concepts
- **Frontend (HTML, CSS, JS):** Provides structure, styling, and interactivity.
- **Backend (PHP):** Handles server logic, processes requests, and connects to Supabase.
- **Database (Supabase/PostgreSQL):** Stores and retrieves structured data.

---

## 🤝 Contribution
Contributions are welcome! To contribute:
1. Fork this repo.
2. Create a feature branch: `git checkout -b feature-name`
3. Commit changes: `git commit -m "Added feature X"`
4. Push branch: `git push origin feature-name`
5. Open a Pull Request.

---

## 📜 License
This project is licensed under the **MIT License**. Feel free to use and modify.

---

## 👨‍💻 Author
**AapkaSathi Team**  
GitHub: [aapkasathi](https://github.com/aapkasathi)
