/**
 * Created by bal_njparker on 5/16/2016.
 */

import ihs.apcs.spacebattle.*;
import ihs.apcs.spacebattle.commands.*;
import ihs.apcs.spacebattle.Point;

import java.awt.*;
import java.util.ArrayList;

public class AsteroidMiner extends BasicSpaceship {

    private enum shipState{
        STARTUP,THRUST,IDLE,SEARCH,BRAKE,RADAR,FIRE,ROTATE
    }
    private shipState state;
    private Point attackPoint;
    private RadarResults radar;
    private ArrayList<ObjectStatus> otherStuff = new ArrayList<>();
    private int radarDataDragonSize;
    private int radarDataShipSize;
    private int radarDataAsteroidSize;

    public RegistrationData registerShip(int numImages, int worldWidth, int worldHeight)
    {
        state = shipState.STARTUP;
        return new RegistrationData("PRAISE JEEBUS", new Color(255, 0, 10), 0);
    }

    @Override
    public ShipCommand getNextCommand(BasicEnvironment env)
    {
        ObjectStatus ship = env.getShipStatus();

        switch(state) {
            case STARTUP:
                state = shipState.THRUST;
                return new ThrustCommand('B',2,0.5);
            case THRUST:
                state = shipState.SEARCH;
                return new RadarCommand(4);
            case ROTATE:
                state = shipState.RADAR;
                return new RadarCommand(4);
            case IDLE:
                state = shipState.RADAR;
                return new IdleCommand(0.1);
            case SEARCH:
                radar = env.getRadar();
                radarDataDragonSize = radar.getByType("Dragon").size();
                radarDataShipSize = radar.getByType("Ship").size();
                radarDataAsteroidSize = radar.getByType("Asteroid").size();
                state = shipState.FIRE;
                if (radarDataDragonSize > 0){
                    otherStuff = (ArrayList)radar.getByType("Dragon");
                    state = shipState.FIRE;
                    return new RotateCommand(ship.getPosition().getAngleTo(otherStuff.get(0).getPosition()) - ship.getOrientation());
                } else if (radarDataShipSize > 0){
                    otherStuff = (ArrayList)radar.getByType("Ship");
                    state = shipState.FIRE;
                    return new RotateCommand(ship.getPosition().getAngleTo(otherStuff.get(0).getPosition()) - ship.getOrientation());
                } else if (radarDataAsteroidSize > 0){
                    otherStuff = (ArrayList)radar.getByType("Asteroid");
                    state = shipState.FIRE;
                    return new RotateCommand(ship.getPosition().getAngleTo(otherStuff.get(0).getPosition()) - ship.getOrientation());
                } else {
                    if (ship.getSpeed()>100){
                        state = shipState.BRAKE;
                        return new BrakeCommand(0.5);
                    } else {
                        state = shipState.THRUST;
                        return new ThrustCommand('B',0.5,0.5);
                    }
                }
            case BRAKE:
                state = shipState.SEARCH;
                return new RadarCommand(4);
            case RADAR:
                state = shipState.SEARCH;
                return new RadarCommand(4);
            case FIRE:
                state = shipState.IDLE;
                return new FireTorpedoCommand('F');
            default:
                return new IdleCommand(0.1);
        }
    }

}
