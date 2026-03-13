-- =====================================================
-- Tworzenie użytkowników i nadawanie uprawnień
-- =====================================================

-- Pobranie nazwy bazy ze zmiennej środowiskowej (opcjonalnie)
\set dbname `echo $POSTGRES_DB`

-- Utworzenie użytkowników (użyj silnych haseł!)
CREATE USER flyway WITH PASSWORD 'flyway_password';
CREATE USER app_user WITH PASSWORD 'app_password';
CREATE USER dev_user WITH PASSWORD 'dev_password';

-- Przyznanie prawa łączenia się z bazą
GRANT CONNECT ON DATABASE :dbname TO flyway, app_user, dev_user;

-- Przełączenie do właściwej bazy
\connect :dbname;

-- Ustawienie właściciela schematu public na flyway
ALTER SCHEMA public OWNER TO flyway;

-- Nadanie USAGE na schemacie
GRANT USAGE ON SCHEMA public TO flyway, app_user, dev_user;

-- Domyślne uprawnienia dla przyszłych obiektów tworzonych przez flyway
ALTER DEFAULT PRIVILEGES FOR USER flyway IN SCHEMA public
   GRANT SELECT, INSERT, UPDATE, DELETE ON TABLES TO app_user, dev_user;

ALTER DEFAULT PRIVILEGES FOR USER flyway IN SCHEMA public
   GRANT USAGE ON SEQUENCES TO app_user, dev_user;

-- Opcjonalnie: uprawnienia do funkcji
ALTER DEFAULT PRIVILEGES FOR USER flyway IN SCHEMA public
   GRANT EXECUTE ON FUNCTIONS TO app_user, dev_user;

-- Dla bezpieczeństwa odbieramy domyślne uprawnienia PUBLIC
REVOKE CREATE ON SCHEMA public FROM PUBLIC;
REVOKE ALL ON ALL TABLES IN SCHEMA public FROM PUBLIC;
REVOKE ALL ON ALL SEQUENCES IN SCHEMA public FROM PUBLIC;
REVOKE ALL ON ALL FUNCTIONS IN SCHEMA public FROM PUBLIC;