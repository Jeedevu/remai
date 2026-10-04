import { app } from './app';
import { config } from './config/env';

app.listen(config.port, () => {
  console.log(`[REM AI Gateway] listening on port ${config.port} (mode: ${config.nodeEnv})`);
  console.log(`[REM AI Gateway] AI Provider configured: ${config.aiProvider}`);
});
