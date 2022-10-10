package a05.receptionist;

import akka.actor.typed.ActorRef;
import akka.actor.typed.Behavior;
import akka.actor.typed.javadsl.AbstractBehavior;
import akka.actor.typed.javadsl.ActorContext;
import akka.actor.typed.javadsl.Behaviors;
import akka.actor.typed.javadsl.Receive;
import akka.actor.typed.receptionist.Receptionist;

import java.time.Duration;

public class Generator extends AbstractBehavior<Generator.Command> {
    public interface Command {
    }

    private static class ChangeSink implements Command {
        Receptionist.Listing listing;

        public ChangeSink(Receptionist.Listing listing) {
            this.listing = listing;
        }
    }

    private static class SendHeartBeat implements Command {}

    private static ActorRef<String> sink;

    public static Behavior<Generator.Command> create() {
        return Behaviors.setup(
                ctx -> {
                    ActorRef<Receptionist.Listing> listingActorRef = ctx.messageAdapter(Receptionist.Listing.class, ChangeSink::new);
                    ctx
                            .getSystem()
                            .receptionist()
                            .tell(
                                    Receptionist.subscribe(
                                            Sink.SERVICE_KEY, listingActorRef));

                    ctx
                            .getSystem()
                            .scheduler()
                            .scheduleAtFixedRate(Duration.ZERO, Duration.ofSeconds(1),
                                    () -> ctx.getSelf().tell(new SendHeartBeat()),
                                    ctx.getSystem().executionContext());

                    return new Generator(ctx);
                });
    }

    private Generator(ActorContext<Generator.Command> context) {
        super(context);
    }

    @Override
    public Receive<Generator.Command> createReceive() {
        return newReceiveBuilder()
                .onMessage(ChangeSink.class, this::changeSink)
                .onMessage(SendHeartBeat.class, this::sendHeartBeat)
                .build();
    }

    private Behavior<Command> changeSink(ChangeSink msg) {
        getContext().getLog().debug("Got changeSink message");
        sink = msg.listing.allServiceInstances(Sink.SERVICE_KEY).head();
        return this;
    }

    private Behavior<Command> sendHeartBeat(SendHeartBeat msg) {
        getContext().getLog().debug("Sending heartbeat to " + sink);
        if (sink != null) {
            sink.tell("Heartbeat " + getContext().getSelf());
        }
        return this;
    }
}
