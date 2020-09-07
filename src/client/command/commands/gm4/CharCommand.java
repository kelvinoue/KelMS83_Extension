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

import client.MapleJob;
import client.command.Command;
import client.MapleClient;
import client.MapleCharacter;
import client.Skill;
import client.SkillFactory;
import client.inventory.Equip;
import client.inventory.Item;
import client.inventory.MapleInventoryType;
import client.inventory.manipulator.MapleInventoryManipulator;
import constants.inventory.EquipType;
import constants.inventory.ItemConstants;
import java.io.File;
import provider.MapleData;
import provider.MapleDataProviderFactory;
import server.MapleItemInformationProvider;

public class CharCommand extends Command {
    {
        setDescription("Change job/level");
    }
    
    @Override
    public void execute(MapleClient c, String[] params) {
        MapleCharacter player = c.getPlayer();
        if (params.length < 2 || params.length > 4) {
            player.yellowMessage("Usage: !char <job> <level> <mode:g/s/b>");
            return;
        }
        
        MapleCharacter victim = c.getPlayer();
        int jobid = -1;
        int newlevel = -1;
        boolean gmmode = false; //GM mode
        boolean msmode = false; //Manual skills mode
        boolean bypass = false; //Cynugs stat cap bypass
        
        if (params.length == 2) {
            jobid = Integer.parseInt(params[0]);
            newlevel = Integer.parseInt(params[1]);
        }
        else if (params.length == 3) {
            if (params[2].equalsIgnoreCase("g")) {
                jobid = Integer.parseInt(params[0]);
                newlevel = Integer.parseInt(params[1]);
                gmmode = true;
            }
            else if (params[2].equalsIgnoreCase("s")) {
                jobid = Integer.parseInt(params[0]);
                newlevel = Integer.parseInt(params[1]);
                msmode = true;
            }
            else if (params[2].equalsIgnoreCase("b")) {
                jobid = Integer.parseInt(params[0]);
                newlevel = Integer.parseInt(params[1]);
                bypass = true;
            }
            else if (params[2].equalsIgnoreCase("bs") || params[2].equalsIgnoreCase("sb")) {
                jobid = Integer.parseInt(params[0]);
                newlevel = Integer.parseInt(params[1]);
                msmode = true;
                bypass = true;
            }
            else if (params[2].equalsIgnoreCase("bg") || params[2].equalsIgnoreCase("gb")) {
                jobid = Integer.parseInt(params[0]);
                newlevel = Integer.parseInt(params[1]);
                gmmode = true;
                bypass = true;
            }
            else {
                victim = c.getWorldServer().getPlayerStorage().getCharacterByName(params[0]);
                jobid = Integer.parseInt(params[1]);
                newlevel = Integer.parseInt(params[2]);
            }
        }
        else if (params.length == 4) {
            victim = c.getWorldServer().getPlayerStorage().getCharacterByName(params[0]);
            jobid = Integer.parseInt(params[1]);
            newlevel = Integer.parseInt(params[2]);
            if (params[3].equalsIgnoreCase("g")) {
                gmmode = true;
            }
            else if (params[3].equalsIgnoreCase("s")) {
                msmode = true;
            }
            else if (params[3].equalsIgnoreCase("b")) {
                bypass = true;
            }
            else if (params[3].equalsIgnoreCase("bs") || params[3].equalsIgnoreCase("sb")) {
                msmode = true;
                bypass = true;
            }
            else if (params[3].equalsIgnoreCase("bg") || params[3].equalsIgnoreCase("gb")) {
                gmmode = true;
                bypass = true;
            }
        }
        
        if ((jobid < 0 || jobid >= 2200) || newlevel <= 0 || newlevel > 200 || (jobid >= 800 && jobid < 900)) {
            player.message("Please enter a valid job id and level.");
            return;
        }
        
        if (victim == null) {
            player.message("Please enter a valid ign.");
            return;
        }
        
        
        //Update job
        victim.changeJob(MapleJob.getById(jobid));
        victim.equipChanged();
        if (victim.getJob().getId() != jobid) { //Stops if job was not changed successfully
            player.message("Please enter a valid job id and level.");
            return;
        }
        
        
        //Default hpmp, ap, sp, item settings
        if (jobid >= 1000 && jobid < 2000 && newlevel >= 120 && bypass == false) {
            newlevel = 120;
        }
        
        int newhp = 12 * newlevel + 50;
        int newmp = 10 * newlevel + 2;
        int newap = 9 + (newlevel - 1) * 5;
        int newsp = (newlevel - 10) * 3;
        
        
        //Class/level-specific hpmp settings
        //Level 1 chars
        if (newlevel == 1 && gmmode == false) {
            newhp = 50;
            newmp = 5;
        }
        //GMs and gmmode
        else if (jobid == 900 || jobid == 910 || gmmode == true) {
            newhp = 30000;
            newmp = 30000;
        }
        //Warriors, except spearmen
        else if ((jobid >= 100 && jobid < 130  || (jobid >= 1100 && jobid < 1200) || (jobid >= 2100 && jobid < 2200)) && newlevel >= 10) {
            newhp = 24 * newlevel + 472 + /* Accounts for HP incr skill -> */ (40 * (newlevel - 10));
            newmp = 4 * newlevel + 56;
            if (newlevel >= 10 && newlevel < 30) {
                newhp = newhp + 250;
            }
            else if (newlevel >= 30) {
                newhp = newhp + 250;
                if ((jobid >= 110 && jobid <= 112) || (jobid >= 1110 && jobid <= 1112) || (jobid >= 2110 && jobid <= 2112)) {
                    newhp = newhp + 350;
                }
                else if (jobid >= 120 && jobid <= 122) {
                    newmp = newmp + 150;
                }
            }
        }
        //Spearmen
        else if ((jobid >= 130 && jobid < 200) && newlevel >= 10) {
            newhp = 24 * newlevel + 172 + /* Accounts for HP incr skill -> */ (40 * (newlevel - 10));
            newmp = 4 * newlevel + 156;
            if (newlevel >= 10 && newlevel < 30) {
                newhp = newhp + 250;
            }
            else if (newlevel >= 30) {
                newhp = newhp + 250;
                newmp = newmp + 150;
            }
        }
        //Mages
        else if ((jobid >= 200 && jobid < 300 && newlevel >= 8) || (jobid >= 1200 && jobid < 1300 && newlevel >= 10)) {
            newhp = 10 * newlevel + 64;
            newmp = 22 * newlevel + 488 + /* Accounts for MP incr skill -> */ (20 * (newlevel - 8));
            if ((newlevel >= 8 && newlevel < 30 && jobid >= 200 && jobid < 300) || (newlevel >= 10 && newlevel < 30 && jobid >= 1200 && jobid < 1300)) {
                newmp = newmp + 150;
            }
            else if (newlevel >= 30) {
                newmp = newmp + 150;
                if (jobid != 200 && jobid != 2000) {
                    newmp = newmp + 500;
                }
            }
        }
        //Bowmen
        else if (((jobid >= 300 && jobid < 400) || (jobid >= 1300 && jobid < 1400)) && newlevel >= 10) {
            newhp = 20 * newlevel + 378;
            newmp = 14 * newlevel + 148;
            if (newlevel >= 10 && newlevel < 30) {
                newhp = newhp + 200;
            }
            else if (newlevel >= 30) {
                newhp = newhp + 200;
                if (jobid != 300 && jobid != 3000) {
                    newhp = newhp + 350;
                    newmp = newmp + 200;
                }
            }
        }
        //Thieves
        else if (((jobid >= 400 && jobid < 500) || (jobid >= 1400 && jobid < 1500)) && newlevel >= 10) {
            newhp = 20 * newlevel + 378;
            newmp = 14 * newlevel + 148;
            if (newlevel >= 10 && newlevel < 30) {
                newhp = newhp + 200;
            }
            else if (newlevel >= 30) {
                newhp = newhp + 200;
                if (jobid != 400 && jobid != 4000) {
                    newhp = newhp + 350;
                    newmp = newmp + 200;
                }
            }
        }
        //Pirates
        else if (((jobid >= 500 && jobid < 600) || (jobid >= 1500 && jobid < 1600)) && newlevel >= 10) {
            newhp = 22 * newlevel + 380;
            newmp = 18 * newlevel + 111;
            if (newlevel >= 10 && newlevel < 30) {
                newhp = newhp + 200;
            }
            else if (newlevel >= 30) {
                newhp = newhp + 200;
                if (jobid != 500 && jobid != 5000) {
                    newhp = newhp + 250;
                    newmp = newmp + 175;
                }
            }
        }
        
        
        //Class/level-specific ap and sp settings
        //Cygnus ap
        if (jobid >= 1000 && jobid < 2000) {
            if (newlevel <= 70) {
                newap = 9 + (newlevel - 1) * 6;
            }
            else if (newlevel > 70) {
                newap = (9 + (70 - 1) * 6) + ((newlevel - 70) * 5);
            }
        }
        //Mage sp
        if (jobid >= 200 && jobid < 300) {
            newsp = (newlevel - 8) * 3;
        }
        //Beginner sp
        else if (jobid == 0 || jobid == 1000 || jobid == 2000) {
            newsp = 0;
        }
        //GM sp
        else if (jobid == 900 || jobid == 910) {
            newsp = 10;
        }
        //1st job ap/sp
        if (jobid == 100 || jobid == 200 || jobid == 300 || jobid == 400 || jobid == 500 || jobid == 1100 || jobid == 1200 || jobid == 1300 || jobid == 1400 || jobid == 1500 || jobid == 2100) {
            if (newlevel >= 10 || (newlevel >= 8 && jobid == 200)) {
                newsp = newsp + 1;
                newap = newap + 5;
            }
            else {
                newsp = 0;
            }
        }
        //2nd job ap/sp
        else if (jobid == 110 || jobid == 120 || jobid == 130 || jobid == 210 || jobid == 220 || jobid == 230 || jobid == 310 || jobid == 320 || jobid == 410 || jobid == 420 || jobid == 510 || jobid == 520 || jobid == 1110 || jobid == 1210 || jobid == 1310 || jobid == 1410 || jobid == 1510 || jobid == 2110) {
            if ((newlevel >= 10 && newlevel < 30) || (newlevel >= 8 && newlevel < 30 && (jobid == 210 || jobid == 220 || jobid == 230))) {
                newsp = newsp + 1;
                newap = newap + 5;
            }
            else if (newlevel >= 30) {
                newsp = newsp + 2;
                newap = newap + 10;
            }
            else {
                newsp = 0;
            }
        }
        //3rd job ap/sp
        else if (jobid == 111 || jobid == 121 || jobid == 131 || jobid == 211 || jobid == 221 || jobid == 231 || jobid == 311 || jobid == 321 || jobid == 411 || jobid == 421 || jobid == 511 || jobid == 521 || jobid == 1111 || jobid == 1211 || jobid == 1311 || jobid == 1411 || jobid == 1511 || jobid == 2111) {
            if ((newlevel >= 10 && newlevel < 30) || (newlevel >= 8 && newlevel < 30 && (jobid == 211 || jobid == 221 || jobid == 231))) {
                newsp = newsp + 1;
                newap = newap + 5;
            }
            else if (newlevel >= 30 && newlevel < 70) {
                newsp = newsp + 2;
                newap = newap + 10;
            }
            else if (newlevel >= 70) {
                newsp = newsp + 3;
                newap = newap + 15;
            }
            else {
                newsp = 0;
            }
        }
        //4th job ap/sp
        else if (jobid == 112 || jobid == 122 || jobid == 132 || jobid == 212 || jobid == 222 || jobid == 232 || jobid == 312 || jobid == 322 || jobid == 412 || jobid == 422 || jobid == 512 || jobid == 522 || jobid == 1112 || jobid == 1212 || jobid == 1312 || jobid == 1412 || jobid == 1512 || jobid == 2112) {
            if ((newlevel >= 10 && newlevel < 30) || (newlevel >= 8 && newlevel < 30 && (jobid == 212 || jobid == 222 || jobid == 232))) {
                newsp = newsp + 1;
                newap = newap + 5;
            }
            else if (newlevel >= 30 && newlevel < 70) {
                newsp = newsp + 2;
                newap = newap + 10;
            }
            else if (newlevel >= 70 && newlevel < 120) {
                newsp = newsp + 3;
                newap = newap + 15;
            }
            else if (newlevel >= 120) {
                newsp = newsp + 4;
                newap = newap + 20;
            }
            else {
                newsp = 0;
            }
        }
        
        
        //Class/level-specific item settings
        int newitem[] = {0, 0, 0, 0, 0, 0, 0, 0, 0};
        short quantity = 1;
        
        //Warriors, except Aran
        if ((jobid >= 100 && jobid < 200) || (jobid >= 1100 && jobid < 1200)) {
            if (newlevel >= 10 && newlevel < 20) {
                newitem[0] = 1402001; //Wooden sword
                newitem[1] = 1432000; //Spear
                newitem[2] = 1422000; //Wooden Mallet
            }
            else if (newlevel >= 20 && newlevel < 30) {
                newitem[0] = 1402000; //Two-handed sword
                newitem[1] = 1432008; //Fish spear
                newitem[2] = 1432009; //Bamboo spear
                newitem[3] = 1432013; //Pumpkin Spear
                newitem[4] = 1422003; //Square Hammer
            }
            else if (newlevel >= 30 && newlevel < 40) {
                newitem[0] = 1402044; //Pumpkin Lantern
                newitem[1] = 1302020; //Maple Sword
                newitem[2] = 1302013; //Red Whip
                newitem[3] = 1302063; //Flaming Katana
                newitem[4] = 1432002; //Forked Spear
                newitem[5] = 1422001; //Mithril Maul
            }
            else if (newlevel >= 40 && newlevel < 50) {
                newitem[0] = 1402007; //Zard
                newitem[1] = 1302030; //Maple Soul Singer
                newitem[2] = 1432005; //Zeco
                newitem[3] = 1432012; //Maple Impaler
                newitem[4] = 1422007; //Titan
                newitem[5] = 1422014; //Maple Doom Singer
            }
            else if (newlevel >= 50 && newlevel < 60) {
                newitem[0] = 1402013; //Japanese Map
                newitem[1] = 1432004; //Serpent's Tongue
                newitem[2] = 1422005; //Golden Mole
            }
            else if (newlevel >= 60 && newlevel < 70) {
                newitem[0] = 1402011; //Sparta
                newitem[1] = 1402039; //Maple Soul Rohen
                newitem[2] = 1432006; //Holy Spear
                newitem[3] = 1432040; //Maple Soul Spear
                newitem[4] = 1422009; //The Blessing
                newitem[5] = 1422029; //Maple Belzet
            }
            else if (newlevel >= 70 && newlevel < 80) {
                newitem[0] = 1402012; //Doombringer
                newitem[1] = 1432007; //Redemption
                newitem[2] = 1422010; //Gigantic Sledge
            }
            else if (newlevel >= 80 && newlevel < 90) {
                newitem[0] = 1402015; //Heaven's Gate
                newitem[1] = 1432010; //Omega Spear
                newitem[2] = 1422012; //The Morningstar
            }
            else if (newlevel >= 90 && newlevel < 100) {
                newitem[0] = 1402016; //Devil's Sunrise
                newitem[1] = 1432011; //Fairfrozen
                newitem[2] = 1422013; //Leomite
            }
            else if (newlevel >= 100 && newlevel < 110) {
                newitem[0] = 1402037; //Stonetooth Sword
                newitem[1] = 1432030; //Pinaka
                newitem[2] = 1422027; //Golden Smith Hammer
            }
            else if (newlevel >= 110 && newlevel < 120) {
                newitem[0] = 1402036; //Dragon Claymore
                newitem[1] = 1432038; //Dragon Faltizan
                newitem[2] = 1422028; //Dragon Flame
            }
            else if (newlevel >= 120) {
                newitem[0] = 1402046; //Timeless Nibleheim
                newitem[1] = 1432047; //Timeless Alchupiz
                newitem[2] = 1422037; //Timeless Bellocce
            }
        }
        //Mages
        else if ((jobid >= 200 && jobid < 300) || (jobid >= 1200 && jobid < 1300)) {
            if (newlevel >= 8 && newlevel < 20) {
                newitem[0] = 1382000; //Wooden Staff
                newitem[1] = 1372005; //Wooden Wand
                newitem[2] = 1092008; //Pan Lid
            }
            else if (newlevel >= 20 && newlevel < 30) {
                newitem[0] = 1382004; //Old Wooden Staff
                newitem[1] = 1372002; //Metal Wand
                newitem[2] = 1092021; //Mystic Shield
            }
            else if (newlevel >= 30 && newlevel < 40) {
                newitem[0] = 1382017; //Circle-Winded Staff
                newitem[1] = 1382009; //Maple staff
                newitem[2] = 1372003; //Mithril Wand
                newitem[3] = 1092029; //Esther Shield
            }
            else if (newlevel >= 40 && newlevel < 52) {
                newitem[0] = 1382019; //Hall Staff
                newitem[1] = 1382012; //Maple Lama Staff
                newitem[2] = 1372000; //Fairy Wand
                newitem[3] = 1372017; //Streetlight
            }
            else if (newlevel >= 52 && newlevel < 64) {
                newitem[0] = 1382014; //Sun Quan Staff
                newitem[1] = 1372011; //Zhu-Ge-Liang Wand
                newitem[2] = 1372008; //Hinomaru Fan
            }
            else if (newlevel >= 64 && newlevel < 78) {
                newitem[0] = 1382039; //Maple Wisdom Staff
                newitem[1] = 1372034; //Maple Shine Wand
                newitem[2] = 1092045; //Maple Magician shield
            }
            else if (newlevel >= 78 && newlevel < 88) {
                newitem[0] = 1382010; //Dark Ritual
                newitem[1] = 1372016; //Phoenix Wand
            }
            else if (newlevel >= 88 && newlevel < 98) {
                newitem[0] = 1382008; //Kage
                newitem[1] = 1372009; //Magicodar
            }
            else if (newlevel >= 98 && newlevel < 110) {
                newitem[0] = 1382035; //Blue Marine
                newitem[1] = 1372010; //Dimon Wand
            }
            else if (newlevel >= 110 && newlevel < 120) {
                newitem[0] = 1382036; //Dragon Staff
                newitem[1] = 1372032; //Dragon Wand
            }
            else if (newlevel >= 120) {
                newitem[0] = 1382057; //Timeless Aeas Hand
                newitem[1] = 1372044; //Timeless Enreal Tear
            }
        }
        //Bowmen
        else if ((jobid >= 300 && jobid < 400) || (jobid >= 1300 && jobid < 1400)) {
            if (newlevel >= 10 && newlevel < 20) {
                newitem[0] = 1452002; //War Bow
                newitem[1] = 1462001; //Crossbow
                newitem[2] = 2060003; //Red arrows - bow
                newitem[3] = 2061003; //Blue arrows - xbow
            }
            else if (newlevel >= 20 && newlevel < 30) {
                newitem[0] = 1452001; //Hunter's Bow
                newitem[1] = 1462002; //Battle Crossbow
                newitem[2] = 2060003; //Red arrows - bow
                newitem[3] = 2061003; //Blue arrows - xbow
            }
            else if (newlevel >= 30 && newlevel < 40) {
                newitem[0] = 1452005; //Ryden
                newitem[1] = 1452016; //Maple Bow
                newitem[2] = 1462000; //Mountain Crossbow
                newitem[3] = 1462014; //Maple Crow
                newitem[4] = 2060003; //Red arrows - bow
                newitem[5] = 2061003; //Blue arrows - xbow
            }
            else if (newlevel >= 40 && newlevel < 50) {
                newitem[0] = 1452007; //Vaulter 2000
                newitem[1] = 1452022; //Maple Soul Searcher
                newitem[2] = 1462005; //Heckler
                newitem[3] = 1462019; //Maple Crossbow
                newitem[4] = 2060003; //Red arrows - bow
                newitem[5] = 2061003; //Blue arrows - xbow
            }
            else if (newlevel >= 50 && newlevel < 60) {
                newitem[0] = 1452008; //Olympus
                newitem[1] = 1462007; //Rower
                newitem[2] = 2060003; //Red arrows - bow
                newitem[3] = 2061003; //Blue arrows - xbow
            }
            else if (newlevel >= 60 && newlevel < 70) {
                newitem[0] = 1452004; //Asianic Bow
                newitem[1] = 1452045; //Maple Kandiva Bow
                newitem[2] = 1462008; //Golden Crow
                newitem[3] = 1462040; //Maple Nishada
                newitem[4] = 2060003; //Red arrows - bow
                newitem[5] = 2061003; //Blue arrows - xbow
            }
            else if (newlevel >= 70 && newlevel < 80) {
                newitem[0] = 1452011; //Golden Hinkel
                newitem[1] = 1462009; //Gross Jaeger
                newitem[2] = 2060003; //Red arrows - bow
                newitem[3] = 2061003; //Blue arrows - xbow
            }
            else if (newlevel >= 80 && newlevel < 90) {
                newitem[0] = 1452015; //Dark Arund
                newitem[1] = 1462013; //Dark Raven
                newitem[2] = 2060003; //Red arrows - bow
                newitem[3] = 2061003; //Blue arrows - xbow
            }
            else if (newlevel >= 90 && newlevel < 100) {
                newitem[0] = 1452017; //Metus
                newitem[1] = 1462018; //Casa Crow
                newitem[2] = 2060003; //Red arrows - bow
                newitem[3] = 2061003; //Blue arrows - xbow
            }
            else if (newlevel >= 100 && newlevel < 110) {
                newitem[0] = 1452021; //Dark Nisrock
                newitem[1] = 1462017; //Dark Neschere
                newitem[2] = 2060003; //Red arrows - bow
                newitem[3] = 2061003; //Blue arrows - xbow
            }
            else if (newlevel >= 110 && newlevel < 120) {
                newitem[0] = 1452044; //Dragon Shiner Bow
                newitem[1] = 1462039; //Dragon Shiner Cross
                newitem[2] = 2060003; //Red arrows - bow
                newitem[3] = 2061003; //Blue arrows - xbow
            }
            else if (newlevel >= 120) {
                newitem[0] = 1452057; //Timeless Engaw
                newitem[1] = 1462050; //Timeless Black Beauty
                newitem[2] = 2060003; //Red arrows - bow
                newitem[3] = 2061003; //Blue arrows - xbow
            }
        }
        //Thieves
        else if ((jobid >= 400 && jobid < 500) || (jobid >= 1400 && jobid < 1500)) {            
            if (newlevel >= 10 && newlevel < 20) {
                newitem[0] = 1472000; //Garnier
                newitem[1] = 1332007; //Fruit knife
                newitem[2] = 2070006; //Ilbi
            }
            else if (newlevel >= 20 && newlevel < 30) {
                newitem[0] = 1472006; //Adamantium igor
                newitem[1] = 1332006; //Field dagger
                newitem[2] = 1092018; //Seclusion Wristguard
                newitem[3] = 2070006; //Ilbi
            }
            else if (newlevel >= 30 && newlevel < 40) {
                newitem[0] = 1472010; //Adamantium guards
                newitem[1] = 1472030; //Maple claw
                newitem[2] = 1332004; //Forked dagger
                newitem[3] = 1332020; //Korean fan
                newitem[4] = 1092022; //Palette
                newitem[5] = 2070006; //Ilbi
            }
            else if (newlevel >= 40 && newlevel < 50) {
                newitem[0] = 1472017; //Dark avarice
                newitem[1] = 1472032; //Maple kandayo
                newitem[2] = 1332021; //Plastic bottle
                newitem[3] = 1332025; //Maple wagner
                newitem[4] = 2070006; //Ilbi
            }
            else if (newlevel >= 50 && newlevel < 60) {
                newitem[0] = 1472021; //Dark slain
                newitem[1] = 1472064; //Neva
                newitem[2] = 1472054; //Shinobi bracer / Musashi claw
                newitem[3] = 1332003; //Shinkita
                newitem[4] = 1332054; //Diamond dagger
                newitem[5] = 1332029; //Liu bei dagger
                newitem[6] = 2070006; //Ilbi
            }
            else if (newlevel >= 60 && newlevel < 70) {
                newitem[0] = 1472025; //Dark gigantic
                newitem[1] = 1472055; //Maple skanda
                newitem[2] = 1332015; //Deadly fin
                newitem[3] = 1332055; //Maple dark mate
                newitem[4] = 1092047; //Maple Thief shield
                newitem[5] = 2070006; //Ilbi
            }
            else if (newlevel >= 70 && newlevel < 80) {
                newitem[0] = 1472029; //Black scarab
                newitem[1] = 1332018; //Kandine
                newitem[2] = 1332053; //Kebob
                newitem[3] = 2070006; //Ilbi
            }
            else if (newlevel >= 80 && newlevel < 90) {
                newitem[0] = 1472031; //Black mamba
                newitem[1] = 1332023; //Dragon's tail
                newitem[2] = 2070006; //Ilbi
            }
            else if (newlevel >= 90 && newlevel < 100) {
                newitem[0] = 1472033; //Casters
                newitem[1] = 1332027; //Varkit
                newitem[2] = 2070006; //Ilbi
            }
            else if (newlevel >= 100 && newlevel < 110) {
                newitem[0] = 1472053; //Red craven
                newitem[1] = 1332052; //Blood dagger
                newitem[2] = 2070006; //Ilbi
            }
            else if (newlevel >= 110 && newlevel < 120) {
                newitem[0] = 1472052; //Dragon purple sleeve
                newitem[1] = 1332050; //Dragon kreda
                newitem[2] = 2070006; //Ilbi
            }
            else if (newlevel >= 120) {
                newitem[0] = 1472068; //Timeless lampion
                newitem[1] = 1332074; //Timeless killic
                newitem[2] = 2070006; //Ilbi
            }
        }
        //Pirates
        else if ((jobid >= 500 && jobid < 600) || (jobid >= 1500 && jobid < 1600)) {
            if (newlevel >= 10 && newlevel < 20) {
                newitem[0] = 1482000; //Steel Knuckler
                newitem[1] = 1492000; //Pistol
                newitem[2] = 2330000; //Bullet
            }
            else if (newlevel >= 20 && newlevel < 30) {
                newitem[0] = 1482002; //Double Tail Knuckler
                newitem[1] = 1492002; //The Negotiator
                newitem[2] = 2330000; //Bullet
            }
            else if (newlevel >= 30 && newlevel < 40) {
                newitem[0] = 1482004; //Prime Hands
                newitem[1] = 1482020; //Maple Knuckle
                newitem[2] = 1492004; //Cold Mind
                newitem[3] = 1492020; //Maple Gun
                newitem[4] = 2330001; //Split Bullet
            }
            else if (newlevel >= 40 && newlevel < 50) {
                newitem[0] = 1482006; //Neozard
                newitem[1] = 1482021; //Maple Storm Finger
                newitem[2] = 1492006; //Lunar Shooter
                newitem[3] = 1492021; //Maple Storm Pistol
                newitem[4] = 2330001; //Split Bullet
            }
            else if (newlevel >= 50 && newlevel < 60) {
                newitem[0] = 1482007; //Fury Claw
                newitem[1] = 1492007; //Mr. Rasfelt
                newitem[2] = 2330002; //Mighty Bullet
            }
            else if (newlevel >= 60 && newlevel < 70) {
                newitem[0] = 1482008; //Psycho Claw
                newitem[1] = 1482022; //Maple Golden Claw
                newitem[2] = 1492008; //Burning Hell
                newitem[3] = 1492022; //Maple Canon Shooter
                newitem[4] = 2330002; //Mighty Bullet
            }
            else if (newlevel >= 70 && newlevel < 80) {
                newitem[0] = 1482009; //Beia Crash
                newitem[1] = 1492009; //Abyss Shooter
                newitem[2] = 2330003; //Vital Bullet
            }
            else if (newlevel >= 80 && newlevel < 90) {
                newitem[0] = 1482010; //Steelno
                newitem[1] = 1492010; //Infinity's Wrath
                newitem[2] = 2330003; //Vital Bullet
            }
            else if (newlevel >= 90 && newlevel < 100) {
                newitem[0] = 1482011; //White Fangz
                newitem[1] = 1492011; //The Peacemaker
                newitem[2] = 2330004; //Shiny Bullet
            }
            else if (newlevel >= 100 && newlevel < 110) {
                newitem[0] = 1482012; //King Cent
                newitem[1] = 1492012; //Concerto
                newitem[2] = 2330004; //Shiny Bullet
            }
            else if (newlevel >= 110 && newlevel < 120) {
                newitem[0] = 1482013; //Dragon Slash Claw
                newitem[1] = 1492013; //Dragonfire Revolver
                newitem[2] = 2330005; //Eternal Bullet
            }
            else if (newlevel >= 120) {
                newitem[0] = 1482023; //Timeless Equinox
                newitem[1] = 1492023; //Timeless Blindness
                newitem[2] = 2330005; //Eternal Bullet
            }
        }
        //Aran
        else if (jobid >= 2100 && jobid < 2200) {
            if (newlevel >= 10 && newlevel < 20) {
                newitem[0] = 1442000; //Pole arm
                newitem[1] = 1442047; //Yellow Valentine Rose
            }
            else if (newlevel >= 20 && newlevel < 30) {
                newitem[0] = 1442007; //Studded Polearm
                newitem[1] = 1442050; //White Valentine Rose
            }
            else if (newlevel >= 30 && newlevel < 40) {
                newitem[0] = 1442080; //Steel Polearm
            }
            else if (newlevel >= 40 && newlevel < 50) {
                newitem[0] = 1442009; //Crescent Polearm
                newitem[1] = 1442024; //Maple Scorpio
                newitem[2] = 1442049; //Blue Valentine Rose
                newitem[3] = 1442025; //Guan Yu Pole Arm
            }
            else if (newlevel >= 50 && newlevel < 60) {
                newitem[0] = 1442005; //The Nine Dragons
                newitem[1] = 1442046; //Super Snowboard
            }
            else if (newlevel >= 60 && newlevel < 70) {
                newitem[0] = 1442010; //Skylar
                newitem[1] = 1442051; //Maple Karstan
            }
            else if (newlevel >= 70 && newlevel < 80) {
                newitem[0] = 1442008; //The Gold Dragon
            }
            else if (newlevel >= 80 && newlevel < 90) {
                newitem[0] = 1442019; //Eclipse
            }
            else if (newlevel >= 90 && newlevel < 100) {
                newitem[0] = 1442020; //Hellslayer
            }
            else if (newlevel >= 100 && newlevel < 110) {
                newitem[0] = 1442044; //Zedbug
            }
            else if (newlevel >= 110 && newlevel < 120) {
                newitem[0] = 1442045; //Dragon Chelbird
            }
            else if (newlevel >= 120) {
                newitem[0] = 1442063; //Timeless Diesra
            }
        }
        //GMs
        else if (jobid == 900 || jobid == 910) {
            newitem[0] = 1002140; //Wizet Invincible Hat
            newitem[1] = 1322013; //Wizet Secret Agent Suitcase
        }
        
        
        
        //Generate items
        if (victim == c.getPlayer()) { //Only generates items for self, and not when using !c on other players
            int i = 0;
            while (i < newitem.length) {
                if (newitem[i] != 0) {
                    //Ilbi, bullet
                    if (newitem[i] == 2070006 || newitem[i] == 2330000) {
                        quantity = 800;
                    }
                    //Arrows
                    else if (newitem[i] == 2060003 || newitem[i] == 2061003) {
                        quantity = 1000;
                    }
                    //Split bullet
                    else if (newitem[i] == 2330001) {
                        quantity = 1200;
                    }
                    //Mighty bullet
                    else if (newitem[i] == 2330002) {
                        quantity = 1600;
                    }
                    //Vital bullet
                    else if (newitem[i] == 2330003) {
                        quantity = 2200;
                    }
                    //Shiny bullet
                    else if (newitem[i] == 2330004) {
                        quantity = 2600;
                    }
                    //Eternal bullet
                    else if (newitem[i] == 2330005) {
                        quantity = 3000;
                    }
                    
                    MapleItemInformationProvider ii = MapleItemInformationProvider.getInstance();
                    
                    //For equip items
                    if (ItemConstants.getInventoryType(newitem[i]).equals(MapleInventoryType.EQUIP)) {
                        Item item = ii.getEquipById(newitem[i]);
                        Equip eqp = (Equip)item;
                        byte upgrades = 7;
                        short addwatk = 0;
                        short addmatk = 0;
                        short addstr = 0;
                        short adddex = 0;
                        short addint = 0;
                        short addluk = 0;
                        short addacc = 0;
                        //short flag = 0;
                        
                        //Adds stats from 60% scrolls according to item type
                        EquipType et = EquipType.getEquipTypeById(newitem[i]);
                        switch(et) { //Alternative method is to use if(et.name().equals("SWORD"))
                            case SWORD:
                                addwatk = (short)(2 * upgrades);
                                addstr = (short)(1 * upgrades);
                                break;
                            case SWORD_2H:
                                addwatk = (short)(2 * upgrades);
                                addstr = (short)(1 * upgrades);
                                break;
                            case SPEAR:
                                addwatk = (short)(2 * upgrades);
                                addstr = (short)(1 * upgrades);
                                break;
                            case MACE_2H:
                                addwatk = (short)(2 * upgrades);
                                addstr = (short)(1 * upgrades);
                                break;
                            case POLEARM:
                                addwatk = (short)(2 * upgrades);
                                addstr = (short)(1 * upgrades);
                                break;
                            case STAFF:
                                addmatk = (short)(2 * upgrades);
                                addint = (short)(1 * upgrades);
                                break;
                            case WAND:
                                addmatk = (short)(2 * upgrades);
                                addint = (short)(1 * upgrades);
                                break;
                            case BOW:
                                addwatk = (short)(2 * upgrades);
                                addacc = (short)(1 * upgrades);
                                break;
                            case CROSSBOW:
                                addwatk = (short)(2 * upgrades);
                                addacc = (short)(1 * upgrades);
                                break;
                            case CLAW:
                                addwatk = (short)(2 * upgrades);
                                addacc = (short)(1 * upgrades);
                                break;
                            case DAGGER:
                                addwatk = (short)(2 * upgrades);
                                addluk = (short)(1 * upgrades);
                                break;
                            case KNUCKLER:
                                addwatk = (short)(2 * upgrades);
                                addstr = (short)(1 * upgrades);
                                break;
                            case PISTOL:
                                addwatk = (short)(2 * upgrades);
                                addacc = (short)(1 * upgrades);
                                break;
                            default:
                                upgrades = 0;
                                break;
                        }
                        
                        eqp.setUpgradeSlots((byte)(eqp.getUpgradeSlots() - upgrades));
                        eqp.setLevel((byte)(eqp.getLevel() + upgrades));
                        eqp.setWatk((short)(eqp.getWatk() + addwatk));
                        eqp.setMatk((short)(eqp.getMatk() + addmatk));
                        eqp.setStr((short)(eqp.getStr() + addstr));
                        eqp.setDex((short)(eqp.getDex() + adddex));
                        eqp.setInt((short)(eqp.getInt() + addint));
                        eqp.setLuk((short)(eqp.getLuk() + addluk));
                        eqp.setAcc((short)(eqp.getAcc() + addacc));
                        //eqp.setFlag(flag);
                        MapleInventoryManipulator.addFromDrop(c, (Equip) item, false, -1);
                    }
                    
                    //For use items
                    else {
                        Item item = new Item(newitem[i], (short) 0, quantity, -1);
                        MapleInventoryManipulator.addFromDrop(c, item, false, -1);
                    }
                }
                
                else {
                    break;
                }
                i++;
            }
        }
        
        //Update level
        victim.loseExp(victim.getExp(), false, false);
        victim.setLevel(Math.min(newlevel, victim.getMaxClassLevel()) - 1);
        victim.levelUp(false);
        
        //Update hpmp
        //int extraHp = victim.getCurrentMaxHp() - victim.getClientMaxHp();
        //int extraMp = victim.getCurrentMaxMp() - victim.getClientMaxMp();
        //int maxhpUpdate = newhp - extraHp;
        //int maxmpUpdate = newmp - extraMp;
        //victim.updateMaxHpMaxMp(maxhpUpdate, maxmpUpdate);
        //victim.updateHpMp(maxhpUpdate, maxmpUpdate);
        victim.updateMaxHpMaxMp(newhp, newmp);
        victim.updateHpMp(newhp, newmp);
        
        //Update stats and ap
        if (gmmode == true) {
            victim.updateStrDexIntLuk(Short.MAX_VALUE);
            victim.changeRemainingAp(0, false);
        }
        else {
            victim.updateStrDexIntLuk(4);
            victim.changeRemainingAp(newap, false);
        }
        
        //Reset skills
        for (MapleData skill_ : MapleDataProviderFactory.getDataProvider(new File(System.getProperty("wzpath") + "/" + "String.wz")).getData("Skill.img").getChildren()) {
            try {
                Skill skill = SkillFactory.getSkill(Integer.parseInt(skill_.getName()));
                victim.changeSkillLevel(skill, (byte) 0, skill.getMaxLevel(), -1);
            } catch (NumberFormatException nfe) {
                nfe.printStackTrace();
                break;
            } catch (NullPointerException npe) {
            }
        }
        if (victim.getJob().isA(MapleJob.ARAN1) || victim.getJob().isA(MapleJob.LEGEND)) {
            Skill skill = SkillFactory.getSkill(5001005);
            victim.changeSkillLevel(skill, (byte) -1, -1, -1);
        } else {
            Skill skill = SkillFactory.getSkill(21001001);
            victim.changeSkillLevel(skill, (byte) -1, -1, -1);
        }
        
        //Max skills for ms/gmmode
        if (gmmode == true || msmode == false) {
            for (MapleData skill_ : MapleDataProviderFactory.getDataProvider(new File(System.getProperty("wzpath") + "/" + "String.wz")).getData("Skill.img").getChildren()) {
                try {
                    Skill skill = SkillFactory.getSkill(Integer.parseInt(skill_.getName()));
                    victim.changeSkillLevel(skill, (byte) skill.getMaxLevel(), skill.getMaxLevel(), -1);
                } catch (NumberFormatException nfe) {
                    nfe.printStackTrace();
                    break;
                } catch (NullPointerException npe) { }
            }
            if (victim.getJob().isA(MapleJob.ARAN1) || victim.getJob().isA(MapleJob.LEGEND)) {
                Skill skill = SkillFactory.getSkill(5001005);
                victim.changeSkillLevel(skill, (byte) -1, -1, -1);
            } else {
                Skill skill = SkillFactory.getSkill(21001001);
                victim.changeSkillLevel(skill, (byte) -1, -1, -1);
            }
        }
        
        //Update sp
        victim.updateRemainingSp(newsp);

        //QOL prompt
        player.yellowMessage("Please auto-assign your AP or use !str/dex/int/luk, add your skills or use !ms, and use your eq.");
    }
}
