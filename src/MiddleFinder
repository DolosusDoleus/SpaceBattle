/**
 * Created by bal_njparker on 5/3/2016.
 */
import java.awt.Color;
import ihs.apcs.spacebattle.*;
import ihs.apcs.spacebattle.commands.*;

public class MiddleFinder extends BasicSpaceship {
    private enum shipState{
        THRUST,ROTATE,IDLE,BRAKE
    }
    private shipState state;
    private Point midpoint;

    @Override
    public RegistrationData registerShip(int numImages, int worldWidth, int worldHeight)
    {
        state = shipState.ROTATE;
        midpoint = new Point(worldWidth/2,worldHeight/2);
        return new RegistrationData("THE BEES", new Color(0, 255, 0), 0);
    }

    @Override
    public ShipCommand getNextCommand(BasicEnvironment env)
    {
        ObjectStatus ship = env.getShipStatus();

        switch (state){
            case THRUST:
                if (ship.getPosition().getDistanceTo(midpoint)>100) {
                    state = shipState.ROTATE;
                    return new ThrustCommand('B',0.1,0.25);
                } else {
                    state = shipState.BRAKE;
                }
            case ROTATE:
                state = shipState.THRUST;
                return new RotateCommand(ship.getPosition().getAngleTo(this.midpoint) - ship.getOrientation());
            case IDLE:
                if (ship.getPosition().getAngleTo(this.midpoint)==0)
                {
                    state = shipState.THRUST;
                } else {
                    state = shipState.ROTATE;
                }
            case BRAKE:
                if (ship.getPosition().getDistanceTo(midpoint)<100) {
                    return new BrakeCommand(0.0);
                } else {
                    state = shipState.THRUST;
                }
            default:
                return new RotateCommand(45);
        }
    }

}
