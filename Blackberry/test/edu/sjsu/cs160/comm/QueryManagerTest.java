/*
    Copyright 2008 San Jose State University
    
    This file is part of the Blackberry Cinequest client.

    The Blackberry Cinequest client is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    The Blackberry Cinequest client is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with the Blackberry Cinequest client.  If not, see <http://www.gnu.org/licenses/>.
 */

package edu.sjsu.cs160.comm;

import java.util.Vector;

import junit.framework.TestCase;
import edu.sjsu.cs160.comm.cinequestitem.Film;
import edu.sjsu.cs160.comm.cinequestitem.Schedule;
import edu.sjsu.cs160.javase.JavaSEPlatform;

public class QueryManagerTest extends TestCase
{
    private QueryManager mgr;
    protected void setUp() throws Exception
    {
        Platform.setInstance(new JavaSEPlatform());
        mgr = new QueryManager();
    }

    // essentially tests getFilm() and getSchedulesFilm() as they're both used
    // to put
    // Film together.
    public void testFilmSchedules()
    {
        TestCallback callback = new TestCallback();
        mgr.getFilm(1577, callback);
        assertTrue(callback.getResult() instanceof Film);
        Film film = (Film) callback.getResult();
        Vector sched = film.getSchedules();
        assertTrue(((Schedule) sched.elementAt(0)).getStartTime().startsWith("2009-02-28 10:00"));
    }

    public void testSchedulesDay()
    {
        TestCallback callback = new TestCallback();
        mgr.getSchedulesDay("2008-02-28", callback);
        assertTrue(callback.getResult() instanceof Vector);
        Vector schedules = (Vector) callback.getResult();
        for (int i = 0; i < schedules.size(); i++)
        {
            assertTrue(schedules.elementAt(i) instanceof Schedule);
        }
    }

    public void testBeaufort()
    {
        TestCallback callback = new TestCallback();
        mgr.getSchedulesDay("2009-03-07", callback);
        Vector schedules = (Vector) callback.getResult();
        
        boolean found = false;
        for (int i = 0; !found && i < schedules.size(); i++)
        {
            Schedule sched = (Schedule) schedules.elementAt(i);
            if (sched.getItemId() == 500)
            {
                assertEquals("The Last Lullaby", sched.getTitle());
                found = true;
            }
        }
        assertTrue(found);
        callback = new TestCallback();
        mgr.getSchedulesDay("2008-03-08", callback);
        schedules = (Vector) callback.getResult();
        found = false;
        for (int i = 0; !found && i < schedules.size(); i++)
        {
            Schedule sched = (Schedule) schedules.elementAt(i);
            if (sched.getItemId() == 500)
            {
                assertEquals("The Last Lullaby", sched.getTitle());
                found = true;
            }
        }
        assertFalse(found);
    }

    public void testMock2()
    {
        TestCallback callback = new TestCallback();
        mgr.getFilm(1677, callback);
        assertTrue(callback.getResult() instanceof Film);
        Film f = (Film) callback.getResult();
        assertTrue(f.getTitle().startsWith("Shorts 8: In Search for I"));
    }

    public void testIskasJourney()
    {
        TestCallback callback = new TestCallback();
        mgr.getFilm(1687, callback);
        Film film = (Film) callback.getResult();
        String title = film.getTitle();
        assertEquals('\u0026', title.charAt(16));
    }
    
    public void testSpecialItem()
    {
        TestCallback callback = new TestCallback();
        mgr.getSchedulesDay("2009-03-08", callback);
        Vector result = (Vector) callback.getResult();
        boolean found = false;
        for (int i = 0; !found && i < result.size(); i++)
            if (((Schedule) result.elementAt(i)).isMobileItem()) found = true; 
        assertTrue(found);
    }
    
    public void testSpecialEvent450()
    {
        TestCallback callback = new TestCallback();
        mgr.getEventSchedules("special_events", callback);
        Vector result = (Vector) callback.getResult();
        boolean found = false;
        for (int i = 0; !found && i < result.size(); i++)
        {
            Schedule sched = (Schedule) result.elementAt(i);
            if (sched.getItemId() == 560)
            {
                found = true;
                assertTrue(sched.isMobileItem());
            }
        }
        assertTrue(found);        
    }
}
