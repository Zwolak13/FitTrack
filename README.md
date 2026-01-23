# FitTrack

Full-stack nutrition tracking app.  
Backend: Spring Boot + H2  
Frontend: Next.js + Tailwind CSS

---

## Backend

**Tech:** Java 21, Spring Boot, Spring Data JPA, Spring Security (JWT), H2, Lombok, JUnit/Mockito

**Uruchamianie:**
```bash
cd backend
./gradlew bootRun
```

**Testy**
```bash
cd backend
./gradlew test
```

## Frontend
**Tech:** Next.js, React, Tailwind CSS

**Konfiguracja**
1. ```cd frontend```
2. stwórz ```.env.local```
3. Ustaw API URL: ```NEXT_PUBLIC_API_URL=http://localhost:8080/```

**Uruchamianie**
```bash
cd frontend
npm install
npm run dev
```
**Otwórz przeglądarkę**:
1. Frontend: http://localhost:3000/login
2. H2 console: http://localhost:8080/h2-console
