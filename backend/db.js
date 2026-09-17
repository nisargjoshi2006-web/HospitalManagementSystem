import mysql from 'mysql2/promise';
import fs from 'fs';
import path from 'path';
import { fileURLToPath } from 'url';

const __filename = fileURLToPath(import.meta.url);
const __dirname = path.dirname(__filename);

// Default config
let dbConfig = {
  host: 'localhost',
  port: 3306,
  user: 'root',
  password: '',
  database: 'hospital'
};

// Attempt to parse config.properties
try {
  const configPath = path.resolve(__dirname, '../config.properties');
  if (fs.existsSync(configPath)) {
    const lines = fs.readFileSync(configPath, 'utf8').split('\n');
    for (const line of lines) {
      const trimmed = line.trim();
      if (!trimmed || trimmed.startsWith('#')) continue;
      const [key, ...rest] = trimmed.split('=');
      const val = rest.join('=').trim();
      if (key === 'db.user') dbConfig.user = val;
      if (key === 'db.password') dbConfig.password = val;
      if (key === 'db.url') {
        const match = val.match(/:\/\/([^:\/]+)(?::(\d+))?\/([^?]+)/);
        if (match) {
          dbConfig.host = match[1];
          if (match[2]) dbConfig.port = parseInt(match[2]);
          if (match[3]) dbConfig.database = match[3];
        }
      }
    }
  }
} catch (e) {
  console.warn('Could not read config.properties, using defaults:', e.message);
}

const pool = mysql.createPool({
  ...dbConfig,
  waitForConnections: true,
  connectionLimit: 10,
  queueLimit: 0,
  decimalNumbers: true
});

export default pool;
