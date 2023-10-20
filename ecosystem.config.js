module.exports = {
  apps: [
    {
      name: 'carl-bot',
      script: '/usr/lib/jvm/java-17-openjdk-amd64/bin/java',
      args: '-jar carl-bot.jar',
      exp_backoff_restart_delay: 100,
    },
  ],
};
