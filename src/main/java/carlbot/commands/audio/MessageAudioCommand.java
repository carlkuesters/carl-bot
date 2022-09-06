package carlbot.commands.audio;

import carlbot.Bot;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import net.dv8tion.jda.api.entities.AudioChannel;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public abstract class MessageAudioCommand extends AudioCommand<MessageReceivedEvent> {

    protected MessageAudioCommand(Bot bot, String commandPrefix) {
        super(bot, commandPrefix);
    }
    private AudioChannel bestAudioChannel;

    @Override
    public boolean isMatching(MessageReceivedEvent event, String content) {
        if (content.startsWith(commandPrefix)) {
            bestAudioChannel = findBestAudioChannel(event);
            if (bestAudioChannel != null) {
                return true;
            } else {
                event.getChannel().sendMessage("Bringt ja nichts, wenn niemand in einem Voice Channel ist...").queue();
            }
        }
        return false;
    }

    private AudioChannel findBestAudioChannel(MessageReceivedEvent event) {
        Member member = event.getMember();
        AudioChannel userAudioChannel = ((member != null) ? member.getVoiceState().getChannel() : null);
        return ((userAudioChannel != null) ? userAudioChannel : AudioUtility.getCurrentOrBestAudioChannel(event.getGuild()));
    }

    @Override
    protected Guild getGuild(MessageReceivedEvent event) {
        return event.getGuild();
    }

    @Override
    protected AudioChannel getAudioChannelToJoin(MessageReceivedEvent event) {
        return bestAudioChannel;
    }

    protected abstract void play(MessageReceivedEvent event, AudioPlayer audioPlayer);
}
