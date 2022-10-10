package a05.receptionist;

import akka.actor.typed.ActorRef;
import akka.actor.typed.Behavior;
import akka.actor.typed.javadsl.Behaviors;
import akka.actor.typed.receptionist.Receptionist;

public class Guardian {
    public static Behavior<Void> create() {
        return Behaviors.setup(
                        ctx -> {
                            ActorRef<String> sink1 = ctx.spawn(Sink.create(), "sink1");
                            ActorRef<String> sink2 = ctx.spawn(Sink.create(), "sink2");
                            ctx
                                    .getSystem()
                                    .receptionist()
                                    .tell(Receptionist.register(Sink.SERVICE_KEY, sink1));

                            for (int i = 0; i < 10; i++) {
                                ctx.spawnAnonymous(Generator.create());
                            }

                            Thread.sleep(10_000);

                            ctx
                                    .getSystem()
                                    .receptionist()
                                    .tell(Receptionist.register(Sink.SERVICE_KEY, sink2));
                            ctx
                                    .getSystem()
                                    .receptionist()
                                    .tell(Receptionist.deregister(Sink.SERVICE_KEY, sink1));

                            return Behaviors.empty();
                        })
                .unsafeCast(); // Void
    }
}
