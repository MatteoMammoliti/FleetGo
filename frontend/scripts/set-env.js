const fs = require('fs');
const path = require('path');

const targetPath = path.join(__dirname, '../src/environments/environment.ts');

const googleMapsApiKey = process.env.GOOGLE_MAPS_API_KEY || '';
const apiUrl = process.env.API_URL || 'http://localhost:8080';

const envConfigFile = `
export const environment = {
  production: false,
  googleMapsApiKey: '${googleMapsApiKey}',
  apiUrl: '${apiUrl}'
};
`;

const dir = path.dirname(targetPath);
if (!fs.existsSync(dir)) {
  console.log(`Creating directory: ${dir}`);
  fs.mkdirSync(dir, { recursive: true });
}

fs.writeFile(targetPath, envConfigFile, (err) => {
  if (err) {
    console.error(err);
    throw err;
  }
});
