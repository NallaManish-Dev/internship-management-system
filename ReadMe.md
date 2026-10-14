# Internship Management System

A web-based platform that connects students with internship opportunities, helps companies manage internship postings and applicants, and gives administrators tools to oversee the platform.

## Technology Stack

### Frontend
- **React**
- **JavaScript** — not TypeScript
- **CSS** for application styling
- **Bootstrap 5** for responsive layout and UI utilities
- **React Router** for client-side navigation
- **Axios** for HTTP requests to the backend

### Backend
- **Java 21**
- **Spring Boot**
- **Spring Web** for REST APIs
- **Spring Security** for authentication and authorization
- **JWT** for token-based authentication
- **Spring Data JPA / Hibernate** for database access
- **Maven** as the build tool, if the backend is generated with Maven

### Database
- **MySQL**

The backend is the source of truth for authentication, authorization, validation, application workflows, and data access. Frontend route guards improve the user experience but do not replace backend security checks.

## User Roles

### Student
- Register and sign in.
- Create and update a student profile.
- Browse and filter available internships.
- Apply for internships.
- Track submitted applications and their statuses.

### Company
- Manage company information.
- Create, edit, publish, close, or otherwise manage its internship postings according to the business rules.
- View applicants for its own postings.
- Review applications and update permitted statuses.

### Admin
- Access protected administrative features.
- Manage and moderate platform users, companies, internships, and other records according to the requirements.

## Project Structure

The intended repository layout is:

```text
internship-management-system/
├── frontend/                         # React + JavaScript + CSS
│   ├── public/
│   ├── src/
│   │   ├── api/                      # Axios client and API functions
│   │   ├── assets/
│   │   ├── components/               # Shared UI components
│   │   ├── context/                  # Shared application state, if needed
│   │   ├── hooks/
│   │   ├── layouts/
│   │   ├── pages/
│   │   ├── routes/
│   │   ├── styles/
│   │   ├── utils/
│   │   ├── App.jsx
│   │   └── main.jsx
│   ├── .env.example
│   └── package.json
├── backend/                          # Java + Spring Boot
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/
│   │   │   └── resources/
│   │   └── test/
│   ├── .env.example                  # If environment files are used by the project
│   └── pom.xml
├── README.md

```

This is the planned structure. If the repository already uses a different structure, inspect it and follow the existing structure instead of moving files unnecessarily.

## Prerequisites

Install the following before running the project:

- Node.js and npm
- Java Development Kit (JDK) 21
- MySQL Server
- Git
- An IDE or editor, such as VS Code or IntelliJ IDEA

Verify installations:

```bash
node --version
npm --version
java --version
git --version
```

Also verify that MySQL is running and that the database credentials are available locally.

## Configuration

### 1. Create the database

Create a local MySQL database. The database name below is an example; use the actual name configured in the backend:

```sql
CREATE DATABASE internship_management;
```

Use a dedicated local database user where possible. Do not commit database passwords or other secrets.

### 2. Configure the backend

The backend foundation reads these variables (see `backend/.env.example`):

```env
DB_HOST=localhost
DB_PORT=3306
DB_NAME=internship_management
DB_USERNAME=your_local_mysql_username
DB_PASSWORD=your_local_mysql_password
JWT_SECRET=replace_with_a_secure_local_secret
FRONTEND_ORIGIN=http://localhost:5173
SERVER_PORT=8080
ADMIN_BOOTSTRAP_EMAIL=
ADMIN_BOOTSTRAP_PASSWORD=
```

`JWT_SECRET` is required by the backend authentication flow and must be a long random local secret. The real value belongs only in the untracked `backend/.env`; never commit credentials or production secrets.

To provision the first administrator, set `ADMIN_BOOTSTRAP_EMAIL` and `ADMIN_BOOTSTRAP_PASSWORD` in the local backend environment before starting Spring Boot. Provisioning is idempotent and refuses to change an existing non-admin account into an administrator. Public registration never accepts an admin role.

The Spring Boot configuration should map the chosen variables to the datasource, JWT settings, and CORS configuration. Use versioned database migrations if configured in the technical design; do not rely on destructive automatic schema updates in production.

### 3. Configure the frontend

Create `frontend/.env` based on `frontend/.env.example`. For a Vite frontend, the API base URL can be configured like this:

```env
VITE_API_BASE_URL=http://localhost:8080/api/v1
```

Vite exposes frontend variables prefixed with `VITE_` to browser code. **Never put secrets in frontend environment variables**; anything bundled into the frontend can be viewed by users.

The API URL must match the actual backend port and API prefix.

## Run Locally

The exact scripts and configuration depend on the files generated in the repository. Inspect `package.json` and `pom.xml` and use the commands they support.

### 1. Start MySQL

Start the MySQL service and ensure the configured database exists.

### 2. Start the backend

From the backend directory, if the project uses the Maven Wrapper:

```bash
./mvnw spring-boot:run
```

On Windows Command Prompt:

```bat
mvnw.cmd spring-boot:run
```

If the project does not include the Maven Wrapper but Maven is installed, use:

```bash
mvn spring-boot:run
```

The backend commonly runs on `http://localhost:8080`, unless configured otherwise.

### 3. Start the frontend

From the frontend directory:

```bash
npm install
npm run dev
```

For a Vite setup, the frontend commonly runs on `http://localhost:5173`. Use the actual URL printed by the development server.

### 4. Verify the connection

- Open the frontend development URL in a browser.
- Confirm the backend is running and the configured API base URL is correct.
- Test sign-in and role-specific pages once authentication and seed/test accounts are implemented.
- Check the browser console and backend logs if a request fails.
- Do not assume that a feature works until it has been tested against the real backend and database.


### Technology consistency

All project documents must describe the same selected stack:

- **Frontend:** React + JavaScript + CSS (with Bootstrap 5, React Router, and Axios where used).
- **Backend:** Java 21 + Spring Boot + Spring Security + JWT.
- **Database:** MySQL, accessed through Spring Data JPA/Hibernate.
- **API style:** REST API under the documented `/api/v1` prefix.

Do not switch the frontend to TypeScript, replace React with plain HTML pages, or change the backend/database stack unless the project owner explicitly requests it. If an existing generated project differs, report the mismatch before making broad changes.

## Security Notes

- Passwords must be securely hashed by the backend.
- Protected operations must enforce authentication, role permissions, and resource ownership on the server.
- Public registration must not allow users to self-assign the Admin role.
- Do not store secrets in source control.
- Validate data on both the frontend and backend; backend validation is authoritative.
- Do not expose private applicant data to unauthorized users.
- Keep production CORS, token handling, database access, and logging settings secure.

## Development Guidelines

1. Read the project documents before making a substantial change.
2. Implement one coherent feature at a time.
3. Keep controllers, services, repositories, DTOs, and UI components separated by responsibility.
4. Keep frontend API calls centralized and consistent with `API_SPECS.md`.
5. Include loading, empty, error, and success states for data-driven screens.
6. Add or update tests for changed behavior.
7. Run the relevant checks available in the repository and report the actual results.
8. Update documentation whenever a change affects requirements, architecture, setup, or API contracts.

## Current Project Status

Milestone 1 established the frontend foundation in `frontend/`. The authentication milestone now provides real student/company registration, BCrypt password hashing, database-backed login, JWT issuance/validation, protected `/api/v1/auth/me`, and role-aware frontend navigation. Internship workflows and remaining role-specific APIs are still pending.

To run the current frontend foundation:

```bash
cd frontend
copy .env.example .env
npm install
npm run dev
```

To verify a production build:

```bash
cd frontend
npm run build
```

The current application includes the Spring Boot backend, MySQL configuration, versioned migrations, authentication, and role-specific API workflows. Verify any newly added workflow against its API contract and database behavior before treating it as production-ready.


## License

Add the project's chosen license here before publishing or distributing the repository.
