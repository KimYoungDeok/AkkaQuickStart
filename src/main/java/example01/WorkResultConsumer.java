package example01;

import akka.actor.ActorRef;
import akka.actor.UntypedActor;
import akka.contrib.pattern.DistributedPubSubExtension;
import akka.contrib.pattern.DistributedPubSubMediator;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import example01.Master.WorkResult;

public class WorkResultConsumer extends UntypedActor {

  private ActorRef mediator = DistributedPubSubExtension.get(getContext().system()).mediator();
  private LoggingAdapter log = Logging.getLogger(getContext().system(), this);

  {
    mediator.tell(new DistributedPubSubMediator.Subscribe(Master.ResultsTopic, getSelf()), getSelf());
  }

  @Override
  public void onReceive(Object message) {
    if (message instanceof DistributedPubSubMediator.SubscribeAck) {
      // do nothing
    }
    else if (message instanceof  WorkResult) {
      WorkResult workResult = (WorkResult) message;
      log.info("Consumed result: {}", workResult.result);
    }
    else {
      unhandled(message);
    }
  }
}
