package a04.supervision;

import akka.actor.typed.ActorRef;
import akka.actor.typed.Behavior;
import akka.actor.typed.javadsl.AbstractBehavior;
import akka.actor.typed.javadsl.ActorContext;
import akka.actor.typed.javadsl.Behaviors;
import akka.actor.typed.javadsl.Receive;

public class ParentActor extends AbstractBehavior<String> {
    private final ActorRef<String> childActor;

    public static Behavior<String> create() {
        return Behaviors.setup(ParentActor::new);
    }

    private ParentActor(ActorContext<String> context) {
        super(context);
        childActor = context.spawn(ChildActor.create(), "child");
    }

    @Override
    public Receive<String> createReceive() {
        return newReceiveBuilder()
                .onMessage(String.class, this::forward)
                .build();
    }

    private Behavior<String> forward(String in) {
        getContext().getLog().debug("Sending request");
        getContext().getLog().debug("Adres " + getContext().getSelf());

        childActor.tell(in);

        return this;
    }
}
