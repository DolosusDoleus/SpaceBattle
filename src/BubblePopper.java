/**
 * Created by bal_njparker on 5/31/2016.
 */
import ihs.apcs.spacebattle.*;
import ihs.apcs.spacebattle.commands.*;
import ihs.apcs.spacebattle.games.KingOfTheBubbleGameInfo;
import ihs.apcs.spacebattle.Point;

import java.awt.*;
import java.util.ArrayList;

public class BubblePopper extends BasicSpaceship {

    private enum shipState{
        STARTUP,THRUST,ROTATE,FIRE,BRAKE,RADAR,IDLE
    }

    private shipState state;
    private double distance;
    private KingOfTheBubbleGameInfo info;

    private ArrayList<Point> bubbleCenters = new ArrayList<>();

    public RegistrationData registerShip(int numImages, int worldWidth, int worldHeight)
    {
        info = new KingOfTheBubbleGameInfo();
        bubbleCenters = (ArrayList)info.getBubblePositions();
        return new RegistrationData("SANIC HORDGEHEG", new Color(0, 255, 251), 0);
    }

    @Override
    public ShipCommand getNextCommand(BasicEnvironment env)
    {
        ObjectStatus ship = env.getShipStatus();
        bubbleCenters = (ArrayList)info.getBubblePositions();
        distance = ship.getPosition().getDistanceTo(bubbleCenters.get(0));

        switch (state) {
            case STARTUP:
                state = shipState.THRUST;
                return new RotateCommand(ship.getPosition().getAngleTo(this.bubbleCenters.get(0)) - ship.getOrientation());
            case THRUST:
                distance = ship.getPosition().getDistanceTo(bubbleCenters.get(0));
                if ((ship.getPosition().getAngleTo(this.bubbleCenters.get(0)) - ship.getOrientation()>3)||(ship.getPosition().getAngleTo(this.bubbleCenters.get(0)) - ship.getOrientation()<-3)) {
                    state = shipState.ROTATE;
                    return new IdleCommand(0.1);
                } else if (distance>250) {
                    return new ThrustCommand('B',0.1,.25);
                } else {
                    state = shipState.BRAKE;
                    return new IdleCommand(0.1);
                }
            case ROTATE:
                state = shipState.THRUST;
                return new RotateCommand(ship.getPosition().getAngleTo(this.bubbleCenters.get(0)) - ship.getOrientation());
            case IDLE:
                distance = ship.getPosition().getDistanceTo(bubbleCenters.get(0));
                if(distance <=150){
                    state = shipState.BRAKE;
                    return new IdleCommand(0.1);
                } else {
                    state = shipState.STARTUP;
                    return new IdleCommand(0.1);
                }
            case BRAKE:
                state = shipState.IDLE;
                return new BrakeCommand(0.0);
            default:
                state = shipState.IDLE;
                return new IdleCommand(0.1);
        }
    }

}
