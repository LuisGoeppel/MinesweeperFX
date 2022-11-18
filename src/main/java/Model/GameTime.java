package Model;

public class GameTime {

    private long startTime;
    private long passedPausedTime = 0;
    private long stoppedTime;


    /**
     * Saves the current time.
     */
    public void start(){
        startTime = System.currentTimeMillis();
    }

    /**
     * Saves the elapsed time since start.
     */
    public void stop(){
        stoppedTime = (System.currentTimeMillis() - startTime);
    }

    /**
     * Saves the paused time.
     */
    public void resume() { passedPausedTime += (System.currentTimeMillis() - startTime - stoppedTime);}


    //getter of the class
    public long getStoppedTime(){
        return (stoppedTime/1000);
    }

    public long getCurrentTime() {return System.currentTimeMillis();}

    public long getPassedGameTime() {return (System.currentTimeMillis() - startTime - passedPausedTime) / 1000;}

}