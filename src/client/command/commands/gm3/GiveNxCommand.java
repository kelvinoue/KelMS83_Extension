/*
    This file is part of the HeavenMS MapleStory Server, commands OdinMS-based
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
   @Author: Arthur L - Refactored command content into modules, edited by Kelvinjang
*/
package client.command.commands.gm3;

import client.command.Command;
import client.MapleClient;
import client.MapleCharacter;

public class GiveNxCommand extends Command {
    {
        setDescription("Get cash to use in the cash shop");
    }

    @Override
    public void execute(MapleClient c, String[] params) {
        MapleCharacter player = c.getPlayer();
        if (params.length < 1) {
            player.yellowMessage("Usage: !nx <nx, mp, np> <ign> <amount>");
            return;
        }

        String recv, typeStr = "nx";
        int value, type = 1;
        if (params.length > 1) {
            if (params[0].length() == 2) {
                switch (params[0]) {
                    case "mp":  // maplePoint
                        type = 2;
                        break;
                    case "np":  // nxPrepaid
                        type = 4;
                        break;
                    default:
                        type = 1;
                }
                typeStr = params[0];
                
                if (params.length > 2) {
                    recv = params[1];
                    value = Integer.parseInt(params[2]);
                } else {
                    recv = c.getPlayer().getName();
                    value = Integer.parseInt(params[1]);
                }
            } else {
                recv = params[0];
                value = Integer.parseInt(params[1]);
            }
        } else {
            recv = c.getPlayer().getName();
            value = Integer.parseInt(params[0]);
        }

        MapleCharacter victim = c.getWorldServer().getPlayerStorage().getCharacterByName(recv);
        if (victim != null) {
            victim.getCashShop().gainCash(type, value);
            player.message("You have gained " + typeStr.toUpperCase() + ".");
        } else {
            player.message("Player '" + recv + "' could not be found.");
        }
    }
}