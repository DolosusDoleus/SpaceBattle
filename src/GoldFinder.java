/**
 * Created by bal_njparker on 5/23/2016.
 */
import ihs.apcs.spacebattle.*;
import ihs.apcs.spacebattle.commands.*;
import ihs.apcs.spacebattle.Point;

import java.awt.*;
import java.util.ArrayList;

public class GoldFinder extends BasicSpaceship {

    private enum shipState{
        STARTUP,THRUST,IDLE,SEARCH,BRAKE,RADAR,FIRE,ROTATE
    }
    private shipState state;
    private Point goldenAsteroid;
    private RadarResults radar;
    private ArrayList<ObjectStatus> otherStuff = new ArrayList<>();

    public RegistrationData registerShip(int numImages, int worldWidth, int worldHeight)
    {
        state = shipState.STARTUP;
        return new RegistrationData("TRONDALD DUMP", new Color(0, 255, 0), 0);
    }

    @Override
    public ShipCommand getNextCommand(BasicEnvironment env)
    {
        ObjectStatus ship = env.getShipStatus();
        goldenAsteroid = env.getGameInfo().getObjectiveLocation();

        switch(state) {
            case STARTUP:
                state = shipState.THRUST;
                return new RotateCommand(ship.getPosition().getAngleTo(goldenAsteroid) - ship.getOrientation());
            case THRUST:
                state = shipState.RADAR;
                return new ThrustCommand('B',0.5,0.5);
            case ROTATE:
                state = shipState.RADAR;
                return new RadarCommand(4);
            case IDLE:
                state = shipState.RADAR;
                return new IdleCommand(0.1);
            case SEARCH:
                radar = env.getRadar();
                try {
                    otherStuff = (ArrayList) radar.getByType("Asteroid");
                    for (int i = 0; i < otherStuff.size() - 1; i++) {
                        if ((ship.getPosition().getAngleTo(otherStuff.get(i).getPosition()) - ship.getOrientation() < 5) || (ship.getPosition().getAngleTo(otherStuff.get(i).getPosition()) - ship.getOrientation() > -5)) {
                            state = shipState.FIRE;
                            return new IdleCommand(0.1);
                        }
                    }
                } catch (Exception e) {
                    state = shipState.THRUST;
                    return new ThrustCommand('B', 0.5, 0.5);
                }
                if ((ship.getPosition().getAngleTo(goldenAsteroid) - ship.getOrientation() > 3) || (ship.getPosition().getAngleTo(goldenAsteroid) - ship.getOrientation() < -3)) {
                    state = shipState.ROTATE;
                    return new RotateCommand(ship.getPosition().getAngleTo(goldenAsteroid) - ship.getOrientation());
                } else if (ship.getSpeed() > 500) {
                    state = shipState.BRAKE;
                    return new BrakeCommand(0.5);
                } else {
                    state = shipState.THRUST;
                    return new ThrustCommand('B', 0.5, 0.5);
                }
            case BRAKE:
                state = shipState.STARTUP;
                return new RadarCommand(4);
            case RADAR:
                state = shipState.SEARCH;
                return new RadarCommand(4);
            case FIRE:
                state = shipState.SEARCH;
                return new FireTorpedoCommand('F');
            default:
                return new IdleCommand(0.1);
        }
    }

}
