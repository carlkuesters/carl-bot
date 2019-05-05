package carlbot;

import net.dv8tion.jda.core.events.Event;

public abstract class Command<EventType extends Event> {

    public abstract boolean isMatching(EventType event);

    public abstract void parse(EventType event);

    public abstract void execute(EventType event);
}
