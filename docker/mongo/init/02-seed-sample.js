/*
 * ------------------------------------------------------------
 * Script de datos de ejemplo (demo mínima)
 * Inserta 2 documentos en la colección raw_ingest
 * ------------------------------------------------------------
 */

(function () {
  const dbName = 'friends_agenda';
  const database = db.getSiblingDB(dbName);

  database.raw_ingest.insertMany([
    {
      type: 'person',
      payload: {
        canonicalId: 'p-1',
        name: { full: 'Alice Example' },
        emails: [{ value: 'alice@example.com' }],
      },
      ingestedAt: new Date(),
    },
    {
      type: 'event',
      payload: {
        canonicalId: 'e-1',
        title: 'Demo Event',
        startsAt: new Date(),
        location: { city: 'Bogotá' },
      },
      ingestedAt: new Date(),
    },
  ]);
})();

