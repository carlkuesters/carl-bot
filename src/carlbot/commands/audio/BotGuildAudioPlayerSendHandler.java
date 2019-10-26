package carlbot.commands.audio;

import carlbot.Bot;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import net.dv8tion.jda.api.entities.Guild;

public class BotGuildAudioPlayerSendHandler extends AudioPlayerSendHandler {

    BotGuildAudioPlayerSendHandler(AudioPlayer audioPlayer, Bot bot, Guild guild) {
        super(audioPlayer);
        this.bot = bot;
        this.guild = guild;
    }
    private Bot bot;
    private Guild guild;

    @Override
    public boolean canProvide() {
        boolean canProvide = super.canProvide();
        bot.setPlayingAudioInGuild(guild, canProvide);
        return canProvide;
    }
}