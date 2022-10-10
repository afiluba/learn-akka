package a04.supervision;

import akka.actor.typed.Behavior;
import akka.actor.typed.SupervisorStrategy;
import akka.actor.typed.javadsl.AbstractBehavior;
import akka.actor.typed.javadsl.ActorContext;
import akka.actor.typed.javadsl.Behaviors;
import akka.actor.typed.javadsl.Receive;

public class ChildActor extends AbstractBehavior<String> {
    private int counter = 0;

    public static Behavior<String> create() {
        Behavior<String> behavior = Behaviors.setup(ChildActor::new);

        return Behaviors.supervise(Behaviors.supervise(behavior)
                        .onFailure(IllegalStateException.class, SupervisorStrategy.restart()))
                .onFailure(IllegalArgumentException.class, SupervisorStrategy.resume());
    }

    private ChildActor(ActorContext<String> context) {
        super(context);
    }

    @Override
    public Receive<String> createReceive() {
        return newReceiveBuilder()
                .onMessageEquals("OK", this::ok)
                .onMessageEquals("restart", this::restart)
                .onMessageEquals("resume", this::resume)
                .build();
    }

    private Behavior<String> ok() {
        getContext().getLog().debug("Got message " + counter++);
        getContext().getLog().debug("Adres " + getContext().getSelf());

        return this;
    }

    private Behavior<String> restart() {
        getContext().getLog().debug("Got message restart");
        getContext().getLog().debug("Adres " + getContext().getSelf());

        throw new IllegalStateException("Exception");
    }

    private Behavior<String> resume() {
        getContext().getLog().debug("Got message resume");
        getContext().getLog().debug("Adres " + getContext().getSelf());

        throw new IllegalArgumentException("Exception");
    }
}
