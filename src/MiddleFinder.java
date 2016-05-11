/**
 * Created by bal_njparker on 5/3/2016.
 */
import java.awt.Color;
import ihs.apcs.spacebattle.*;
import ihs.apcs.spacebattle.commands.*;

public class MiddleFinder extends BasicSpaceship {
    private enum shipState{
        STARTUP,THRUST,ROTATE,IDLE,BRAKE
    }
    private shipState state;
    private Point midpoint;

    @Override
    public RegistrationData registerShip(int numImages, int worldWidth, int worldHeight)
    {
        state = shipState.STARTUP;
        midpoint = new Point(worldWidth/2,worldHeight/2);
        return new RegistrationData("Battlestar Galactica", new Color(255, 255, 0), 0);
    }

    @Override
    public ShipCommand getNextCommand(BasicEnvironment env)
    {
        ObjectStatus ship = env.getShipStatus();
        double distance = ship.getPosition().getDistanceTo(midpoint);

        switch (state) {
            case STARTUP:
                state = shipState.THRUST;
                return new RotateCommand(ship.getPosition().getAngleTo(this.midpoint) - ship.getOrientation());
            case THRUST:
                distance = ship.getPosition().getDistanceTo(midpoint);
                if ((ship.getPosition().getAngleTo(this.midpoint) - ship.getOrientation()>3)||(ship.getPosition().getAngleTo(this.midpoint) - ship.getOrientation()<-3)) {
                    state = shipState.ROTATE;
                    return new IdleCommand(0.00001);
                } else if (distance>250) {
                    return new ThrustCommand('B',0.05,.5);
                } else {
                    state = shipState.BRAKE;
                    return new IdleCommand(0.00001);
                }
            case ROTATE:
                state = shipState.THRUST;
                return new RotateCommand(ship.getPosition().getAngleTo(this.midpoint) - ship.getOrientation());
            case IDLE:
                distance = ship.getPosition().getDistanceTo(midpoint);
                if(distance <=150){
                    state = shipState.BRAKE;
                    return new IdleCommand(0.00001);
                } else {
                    state = shipState.STARTUP;
                    return new IdleCommand(0.00001);
                }
            case BRAKE:
                state = shipState.IDLE;
                return new BrakeCommand(0.0);
            default:
                state = shipState.IDLE;
                return new IdleCommand(0.00001);
        }
    }

}
