const fs = require('fs');
const path = require('path');

const targetPath = path.join(__dirname, '../environment.ts');

const googleMapsApiKey = process.env.GOOGLE_MAPS_API_KEY || '';
const apiUrl = process.env.API_URL || 'http://localhost:8080';

const envConfigFile = `
export const environment = {
  production: false,
  googleMapsApiKey: '${googleMapsApiKey}',
  apiUrl: '${apiUrl}'
};
`;

fs.writeFile(targetPath, envConfigFile, (err) => {
  if (err) {
    console.error(err);
    throw err;
  }
});
