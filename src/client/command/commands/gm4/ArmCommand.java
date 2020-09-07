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
import client.inventory.Equip;
import client.inventory.Item;
import client.inventory.MapleInventoryType;
import client.inventory.manipulator.MapleInventoryManipulator;
import constants.inventory.ItemConstants;
import server.MapleItemInformationProvider;

public class ArmCommand extends Command {
    {
        setDescription("Get endgame armor");
    }
    
    @Override
    public void execute(MapleClient c, String[] params) {
        MapleCharacter player = c.getPlayer();
        boolean strmode = false;
        boolean dexmode = false;
        boolean intmode = false;
        boolean lukmode = false;
        boolean gmmode = false;
        boolean spdmode = false;
        
        if (params.length != 1) {
            player.yellowMessage("Usage: !arm <stat, g, s>");
            return;
        }
        
        if (params[0].equalsIgnoreCase("str")) {
            strmode = true;
        }
        else if (params[0].equalsIgnoreCase("dex")) {
            dexmode = true;
        }
        else if (params[0].equalsIgnoreCase("int")) {
            intmode = true;
        }
        else if (params[0].equalsIgnoreCase("luk")) {
            lukmode = true;
        }
        else if (params[0].equalsIgnoreCase("g")) {
            gmmode = true;
        }
        else if (params[0].equalsIgnoreCase("s")) {
            spdmode = true;
        }
        else {
            player.yellowMessage("Usage: !arm <stat, g, s>");
            return;
        }
        
        //Default armor items
        int newitem[] = {
            1002357, //Zakum helmet
            1050018, //Sauna robe M
            1102084, //Pink gaia cape
            1082149, //Brown wg
            1072344, //Facestompers
            1032003, //Amethyst earrings
            1012098, //Maple leaf str
            1122000, //Horntail necklace
            0 //Placeholder for shields
        };
        
        //For int/matk
        if (intmode == true) {
            newitem[2] = 1102086; //Purple gaia cape
            newitem[4] = 1072239; //Yellow snowshoes
            newitem[6] = 1012102; //Maple leaf int
            newitem[8] = 1092030; //Maple shield
        }
        //For str
        else if (strmode == true) {
            int job = player.getJob().getId();
            if (job >= 100 && job <= 122) {
                newitem[8] = 1092030; //Maple shield
            }
        }
        //For dex
        else if (dexmode == true) {
            newitem[6] = 1012101; //Maple leaf dex
        }
        //For luk
        else if (lukmode == true) {
            newitem[6] = 1012103; //Maple leaf luk
            int job = player.getJob().getId();
            if (job == 400 || (job >= 420 && job <= 422)) {
                newitem[8] = 1092049; //Dragon khanjar
            }
        }
        
        //For females
        if (c.getPlayer().getGender() == 1) {
            newitem[1] = 1051017;
        }
        
        //GM mode
        if (gmmode == true) {
            newitem[0] = 1112407; //Circle of Ancient Thought
            newitem[1] = 0;  //Setting the next item as 0 breaks the loop so that no other items are generated
        }
        //Speed mode
        else if (spdmode == true) {
            newitem[0] = 1112408; //Circle of Ancient Strength
            newitem[1] = 0;  //Setting the next item as 0 breaks the loop so that no other items are generated
        }
        
        //Generate items
        int i = 0;
        while (i < newitem.length) {
            if (newitem[i] != 0) {
                MapleItemInformationProvider ii = MapleItemInformationProvider.getInstance();
                if (ItemConstants.getInventoryType(newitem[i]).equals(MapleInventoryType.EQUIP)) {
                    Item item = ii.getEquipById(newitem[i]);
                    Equip eqp = (Equip)item;
                    //short flag = 0;
                    byte upgrades = 7;
                    byte addupgrades = 0;
                    short addwatk = 0;
                    short addmatk = 0;
                    short addstr = 0;
                    short adddex = 0;
                    short addint = 0;
                    short addluk = 0;
                    short addacc = 0;
                    short addavoid = 0;
                    short addwdef = 0;
                    short addmdef = 0;
                    short addhp = 0;
                    short addmp = 0;
                    short addspeed = 0;
                    short addjump = 0;
                    
                    switch (newitem[i]) {
                        case 1002357: //Zakum helmet
                            upgrades = 10;
                            if (intmode == true) {
                                addint = (short)(2 * upgrades);
                            }
                            else {
                                adddex = (short)(2 * upgrades);
                            }
                            break;
                            
                        case 1051017: //Sauna robe F - overlaps to next case
                            ;
                            
                        case 1050018: //Sauna robe M
                            addwdef = -30;
                            addavoid = -10;
                            addspeed = 0;
                            if (intmode == true) { //Stats tagged to Dark Bazura/Varuna (Lv100)
                                upgrades = 10;
                                addint = (short)(7 + (2 * upgrades)); //Overall int 60% - Int/2, Mdef/1
                                addluk = (short)(4);
                                addwdef = (short)(addwdef + 84);
                                addmdef = (short)(42 + (1 * upgrades));
                                addmp = (short)(45);
                            }
                            else if (strmode == true) {
                                int job = player.getJob().getId();
                                if ((job >= 500 & job < 600) | (job >= 1500 && job < 1600)) { //Pirates: stats tagged to Red Belly Duke (Lv 100)
                                    upgrades = 10;
                                    addstr = (short)(7 + (2 * upgrades)); //Overall str 60% - Str/2, Wdef/1
                                    adddex = (short)(3);
                                    addavoid = (short)(addavoid + 2);
                                    addwdef = (short)(addwdef + 105 + (1 * upgrades));
                                }
                                else { //Warriors: stats tagged to Dark Commodore set (Lv 90)
                                    upgrades = 10;
                                    addupgrades = 4;
                                    addstr = (short)(8 + (2 * 7)); //Top str 60% - Str/2 + Bot dex 60% - Dex/2, Speed/1
                                    adddex = (short)(2 + (2 * 7));
                                    addacc = (short)(3);
                                    addwdef = (short)(addwdef + 171);
                                    addmdef = (short)(6);
                                    addspeed = (short)(addspeed + (1 * 7));
                                }
                            }
                            else if (dexmode == true) {
                                int job = player.getJob().getId();
                                if ((job >= 500 & job < 600) | (job >= 1500 && job < 1600)) { //Pirates: stats tagged to Red Belly Duke (Lv 100)
                                    upgrades = 10;
                                    addstr = (short)(7);
                                    adddex = (short)(3 + (2 * upgrades)); //Overall dex 60% - Dex/2, Acc/1
                                    addacc = (short)(1 * upgrades);
                                    addavoid = (short)(addavoid + 2);
                                    addwdef = (short)(addwdef + 105);
                                }
                                else { //Bowmen: Stats tagged to Red Armis/Arzuna (Lv 100)
                                    upgrades = 10;
                                    adddex = (short)(7 + (2 * upgrades)); //Overall dex 60% - Dex/2, Acc/1
                                    addstr = (short)(6);
                                    addacc = (short)(1 * upgrades);
                                    addavoid = (short)(addavoid + 2);
                                    addwdef = (short)(addwdef + 105);
                                }
                            }
                            else if (lukmode == true) { //Stats tagged to Red Osfa set (Lv 90)
                                upgrades = 10;
                                addupgrades = 4;
                                addluk = (short)(8 + (2 * 7)); //Top luk 60% - Luk/2, Avoid/1 + Bot dex 60% - Dex/2, Speed/1
                                adddex = (short)(4 + (2 * 7));
                                addavoid = (short)(addavoid + (1 * 7));
                                addwdef = (short)(addwdef + 127);
                                addmp = (short)(20);
                                addspeed = (short)(addspeed + (1 * 7));
                            }
                            break;
                            
                        case 1102084: //Pink gaia cape
                            upgrades = 5;
                            addwatk = (short)(2 * upgrades);
                            break;
                            
                        case 1102086: //Purple gaia cape
                            upgrades = 5;
                            addint = (short)(2 * upgrades);
                            break;
                            
                        case 1082149: //Brown wg
                            upgrades = 7;
                            if (intmode == true) {
                                addmatk = (short)(1 * upgrades);
                                addint = (short)(1 * upgrades);
                            }
                            else {
                                addwatk = (short)(2 * upgrades);
                            }
                            break;
                            
                        case 1072344: //Facestompers
                            upgrades = 5;
                            addwatk = (short)(2 * upgrades);
                            break;
                            
                        case 1072239: //Yellow snowshoes
                            upgrades = 7;
                            addint = (short)(1 * upgrades);
                            break;
                            
                        case 1032003: //Amethyst earrings
                            upgrades = 5;
                            if (intmode == true) {
                                addmatk = (short)(2 * upgrades);
                                addint = (short)(1 * upgrades);
                            }
                            else if (lukmode == true) {
                                addluk = (short)(2 * upgrades);
                            }
                            else {
                                adddex = (short)(2 * upgrades);
                            }
                            break;
                            
                        case 1012098: //Maple leaf str
                            upgrades = 7;
                            addstr = (short)(2 * upgrades);
                            break;
                            
                        case 1012101: //Maple leaf dex
                            upgrades = 7;
                            adddex = (short)(2 * upgrades);
                            break;
                            
                        case 1012102: //Maple leaf int
                            upgrades = 7;
                            addint = (short)(2 * upgrades);
                            break;
                            
                        case 1012103: //Maple leaf luk
                            upgrades = 7;
                            addluk = (short)(2 * upgrades);
                            break;
                            
                        case 1122000: //Horntail necklace
                            upgrades = 3;
                            addstr = 15;
                            adddex = 15;
                            addint = 15;
                            addluk = 15;
                            addwdef = 140;
                            addmdef = 140;
                            addavoid = 15;
                            if (intmode == true) {
                                addint = (short)(addint + (2 * (upgrades - 1)));
                            }
                            else if (strmode == true) {
                                addstr = (short)(addstr + (2 * (upgrades - 1)));
                            }
                            else if (dexmode == true) {
                                adddex = (short)(adddex + (2 * (upgrades - 1)));
                            }
                            else if (lukmode == true) {
                                addluk = (short)(addluk + (2 * (upgrades - 1)));
                            }
                            break;
                            
                        case 1092030: //Maple shield
                            upgrades = 10;
                            if (intmode == true) {
                                addint = (short)(1 * upgrades);
                                addmatk = (short)(2 * upgrades);
                            }
                            else if (strmode == true) {
                                addstr = (short)(1 * upgrades);
                                addwatk = (short)(2 * upgrades);
                            }
                            break;
                            
                        case 1092049: //Dragon khanjar
                            upgrades = 7;
                            addstr = (short)(1 * upgrades);
                            addwatk = (short)(2 * upgrades);
                            break;
                            
                        case 1112407: //Circle of Ancient Thought (gmmode)
                            upgrades = 0;
                            addwatk = 999;
                            addmatk = 999;
                            addint = -2;
                            addluk = -2;
                            addacc = 999;
                            addavoid = 999;
                            addwdef = -5 + 999;
                            addmdef = -5 + 999;
                            addspeed = 20;
                            addjump = 10;
                            break;
                            
                        case 1112408: //Circle of Ancient Strength
                            upgrades = 0;
                            addstr = -2;
                            adddex = -2;
                            addwdef = -5;
                            addmdef = -5;
                            addspeed = 40;
                            addjump = 20;
                            break;
                            
                        //Template
                        /*case 0: //
                            upgrades = 0;
                            addint = (short)(0 * upgrades);
                            break;*/
                            
                        default:
                            upgrades = 0;
                            break;
                    }
                    
                    eqp.setUpgradeSlots((byte)(eqp.getUpgradeSlots() - upgrades));
                    eqp.setLevel((byte)(eqp.getLevel() + upgrades + addupgrades));
                    eqp.setWatk((short)(eqp.getWatk() + addwatk));
                    eqp.setMatk((short)(eqp.getMatk() + addmatk));
                    eqp.setStr((short)(eqp.getStr() + addstr));
                    eqp.setDex((short)(eqp.getDex() + adddex));
                    eqp.setInt((short)(eqp.getInt() + addint));
                    eqp.setLuk((short)(eqp.getLuk() + addluk));
                    eqp.setAcc((short)(eqp.getAcc() + addacc));
                    eqp.setAvoid((short)(eqp.getAvoid() + addavoid));
                    eqp.setWdef((short)(eqp.getWdef() + addwdef));
                    eqp.setMdef((short)(eqp.getMdef() + addmdef));
                    eqp.setHp((short)(eqp.getHp() + addhp));
                    eqp.setMp((short)(eqp.getMp() + addmp));
                    eqp.setSpeed((short)(eqp.getSpeed() + addspeed));
                    eqp.setJump((short)(eqp.getJump() + addjump));
                    //eqp.setFlag(flag);
                    MapleInventoryManipulator.addFromDrop(c, (Equip) item, false, -1);
                }
            }
            else {
                break;
            }   
            i++;
        }
    }
}
