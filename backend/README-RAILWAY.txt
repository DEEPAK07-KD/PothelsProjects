RAILWAY DEPLOYMENT
1. Create a Railway project and add PostgreSQL.
2. Deploy this backend folder/repository.
3. Set FRONTEND_URL to your Netlify site URL, for example:
   https://your-site.netlify.app
4. Railway PostgreSQL normally supplies database variables. This project uses DATABASE_URL,
   PGUSER and PGPASSWORD. Confirm they are available in the backend service.
5. Generate a public domain for the backend.
6. Put that public domain in frontend/api-config.js and redeploy Netlify.

IMPORTANT ABOUT PHOTOS:
The current Java code saves uploaded photos to local disk under uploads/.
Railway service filesystems are not suitable for permanent user uploads across redeploys/restarts.
For production, move uploads to persistent object storage (for example Cloudinary/S3-compatible storage)
or attach/configure persistent storage if your Railway setup supports your intended use.

The original hard-coded local PostgreSQL password was removed from application.properties.
