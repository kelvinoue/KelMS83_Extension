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
package client.command.commands.gm2;

import client.command.Command;
import client.MapleClient;
import client.MapleCharacter;
import client.inventory.Item;
import client.inventory.MapleInventoryType;
import client.inventory.manipulator.MapleInventoryManipulator;

public class ClearSlotCommand extends Command {
    {
        setDescription("Clear items to free up space");
    }

    @Override
    public void execute(MapleClient c, String[] params) {
        MapleCharacter player = c.getPlayer();
        if (params.length < 1) {
            player.yellowMessage("Usage: !clearinv <all, eq, use, set, etc, cash>");
            return;
        }
        String type = params[0];
        switch (type) {
            case "all":
                for (int i = 0; i < 101; i++) {
                    Item tempItem = c.getPlayer().getInventory(MapleInventoryType.EQUIP).getItem((byte) i);
                    if (tempItem == null)
                        continue;
                    MapleInventoryManipulator.removeFromSlot(c, MapleInventoryType.EQUIP, (byte) i, tempItem.getQuantity(), false, false);
                }
                for (int i = 0; i < 101; i++) {
                    Item tempItem = c.getPlayer().getInventory(MapleInventoryType.USE).getItem((byte) i);
                    if (tempItem == null)
                        continue;
                    MapleInventoryManipulator.removeFromSlot(c, MapleInventoryType.USE, (byte) i, tempItem.getQuantity(), false, false);
                }
                for (int i = 0; i < 101; i++) {
                    Item tempItem = c.getPlayer().getInventory(MapleInventoryType.ETC).getItem((byte) i);
                    if (tempItem == null)
                        continue;
                    MapleInventoryManipulator.removeFromSlot(c, MapleInventoryType.ETC, (byte) i, tempItem.getQuantity(), false, false);
                }
                for (int i = 0; i < 101; i++) {
                    Item tempItem = c.getPlayer().getInventory(MapleInventoryType.SETUP).getItem((byte) i);
                    if (tempItem == null)
                        continue;
                    MapleInventoryManipulator.removeFromSlot(c, MapleInventoryType.SETUP, (byte) i, tempItem.getQuantity(), false, false);
                }
                for (int i = 0; i < 101; i++) {
                    Item tempItem = c.getPlayer().getInventory(MapleInventoryType.CASH).getItem((byte) i);
                    if (tempItem == null)
                        continue;
                    MapleInventoryManipulator.removeFromSlot(c, MapleInventoryType.CASH, (byte) i, tempItem.getQuantity(), false, false);
                }
                player.message("Inventory cleared.");
                break;
            case "eq": //Shortened from equip
                for (int i = 0; i < 101; i++) {
                    Item tempItem = c.getPlayer().getInventory(MapleInventoryType.EQUIP).getItem((byte) i);
                    if (tempItem == null)
                        continue;
                    MapleInventoryManipulator.removeFromSlot(c, MapleInventoryType.EQUIP, (byte) i, tempItem.getQuantity(), false, false);
                }
                player.message("Eq inventory cleared.");
                break;
            case "use":
                for (int i = 0; i < 101; i++) {
                    Item tempItem = c.getPlayer().getInventory(MapleInventoryType.USE).getItem((byte) i);
                    if (tempItem == null)
                        continue;
                    MapleInventoryManipulator.removeFromSlot(c, MapleInventoryType.USE, (byte) i, tempItem.getQuantity(), false, false);
                }
                player.message("Use inventory cleared.");
                break;
            case "set":
                for (int i = 0; i < 101; i++) {
                    Item tempItem = c.getPlayer().getInventory(MapleInventoryType.SETUP).getItem((byte) i);
                    if (tempItem == null)
                        continue;
                    MapleInventoryManipulator.removeFromSlot(c, MapleInventoryType.SETUP, (byte) i, tempItem.getQuantity(), false, false);
                }
                player.message("Set-up inventory cleared.");
                break;
            case "etc":
                for (int i = 0; i < 101; i++) {
                    Item tempItem = c.getPlayer().getInventory(MapleInventoryType.ETC).getItem((byte) i);
                    if (tempItem == null)
                        continue;
                    MapleInventoryManipulator.removeFromSlot(c, MapleInventoryType.ETC, (byte) i, tempItem.getQuantity(), false, false);
                }
                player.message("Etc inventory cleared.");
                break;
            case "cash":
                for (int i = 0; i < 101; i++) {
                    Item tempItem = c.getPlayer().getInventory(MapleInventoryType.CASH).getItem((byte) i);
                    if (tempItem == null)
                        continue;
                    MapleInventoryManipulator.removeFromSlot(c, MapleInventoryType.CASH, (byte) i, tempItem.getQuantity(), false, false);
                }
                player.message("Cash inventory cleared.");
                break;
            default:
                player.message("Inventory " + type + " does not exist!");
                break;
        }
    }
}
