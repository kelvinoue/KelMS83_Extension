/*
    This file is part of the KelMS Extension for the HeavenMS MapleStory Server, 
    commands OdinMS-based
    Copyleft (L) 2016 - 2019 RonanLana

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU Affero General Public License as
    published by the Free Software Foundation version 3 as published by
    the Free Software Foundation. You may not use, modify or distribute
    this program under any other version of the GNU Affero General Public
    License.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Affero General Public License for more details.

    You should have received a copy of the GNU Affero General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.
*/

/*
   @Author: Kelvinjang
*/
package client.command.commands.gm4;

import client.command.Command;
import client.MapleClient;
import client.MapleCharacter;
import client.inventory.manipulator.MapleInventoryManipulator;

public class PotsCommand extends Command {
    {
        setDescription("Get potions");
    }
    
    @Override
    public void execute(MapleClient c, String[] params) {
        MapleCharacter player = c.getPlayer();
        
        //Class/level-specific item settings
        int newitem[] = {2000005, 2050004, 2022179, 4006001, 4006000}; //Power elix, All cure, Onyx apples, Sumrock, Magrock
        short quantity[] = {400, 200, 100, 50, 50};
        short flag = 0;
        
        //Add item
        int i = 0;
        while (i < newitem.length) {
            MapleInventoryManipulator.addById(c, newitem[i], quantity[i], player.getName(), -1, flag, -1);
            i++;
        }
    }
}
