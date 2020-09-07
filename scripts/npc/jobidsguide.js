/**
 * @author Kelvinjang
 * Info: Jod IDs Guide
 * Script: jobidsguide.js
*/

var status;

var types = ["Adventurers/Explorers", "Knights of Cygnus", "Aran", "Others"];

var jobs = ["Beginner", "Warrior", "Fighter", "Crusader", "Hero", "Page", "White Knight", "Paladin", "Spearman", "Dragon Knight", "Dark Knight", "Magician", "F/P Wizard", "F/P Mage", "F/P Arch Mage", "I/L Wizard", "I/L Mage", "I/L Arch Mage", "Cleric", "Priest", "Bishop", "Bowman", "Hunter", "Ranger", "Bowmaster", "Crossbowman", "Sniper", "Marksman", "Thief", "Assassin", "Hermit", "Night Lord", "Bandit", "Chief Bandit", "Shadower", "Pirate", "Brawler", "Marauder", "Buccaneer", "Gunslinger", "Outlaw", "Corsair", "GM", "Super GM", "Noblesse", "Dawn Warrior (1)", "Dawn Warrior (2)", "Dawn Warrior (3)", "Dawn Warrior (4)", "Blaze Wizard (1)", "Blaze Wizard (2)", "Blaze Wizard (3)", "Blaze Wizard (4)", "Wind Archer (1)", "Wind Archer (2)", "Wind Archer (3)", "Wind Archer (4)", "Night Walker (1)", "Night Walker (2)", "Night Walker (3)", "Night Walker (4)", "Thunder Breaker (1)", "Thunder Breaker (2)", "Thunder Breaker (3)", "Thunder Breaker (4)", "Legend", "Aran (2)", "Aran (3)", "Aran (4)", "Aran (5)"];

var ids = ["0", "100", "110", "111", "112", "120", "121", "122", "130", "131", "132", "200", "210", "211", "212", "220", "221", "222", "230", "231", "232", "300", "310", "311", "312", "320", "321", "322", "400", "410", "411", "412", "420", "421", "422", "500", "510", "511", "512", "520", "521", "522", "900", "910", "1000", "1100", "1110", "1111", "1112", "1200", "1210", "1211", "1212", "1300", "1310", 
"1311", "1312", "1400", "1410", "1411", "1412", "1500", "1510", "1511", "1512", "2000", "2100", "2110", "2111", "2112"];

function start() {
        status = -1;
        action(1, 0, 0);
}

function action(mode, type, selection) {
        if (mode == -1) {
                cm.dispose();
        } 
        else {
                if (mode == 0 && type > 0) {
                        cm.dispose();
                        return;
                }

                if (mode == 1)
                        status++;
                else
                        status--;

                if (status == 0) {
                        var sendStr = "Please select a character class:\r\n#b";

                        for (var i = 0; i < types.length; i++) {
                            sendStr += "#L" + i + "#" + types[i] + "#l\r\n";
                        }

                        cm.sendSimple(sendStr);
                } 
                else if (status == 1) {
                        if (selection > 6) {
                                selection = 6;
                        } else if (selection < 0) {
                                selection = 0;
                        }
                        
                        var sendStr = "List of available jobs:\r\n#b";
                        if (selection == 0) {
                                //sendStr += "\r\n#bAdventurers/Explorers:#k\r\n";
                                for (var i = 0; i < 42; i++) {
                                        sendStr += "#L" + i + "#" + ids[i] + " - " + jobs[i] + "#l\r\n";
                                }
                        }
                        else if (selection == 1) {
                                //sendStr += "\r\n\r\n#bKnights of Cygnus:#k\r\n";
                                for (var i = 44; i < jobs.length - 5; i++) {
                                        sendStr += "#L" + i + "#" + ids[i] + " - " + jobs[i] + "#l\r\n";
                                    }
                        }
                        else if (selection == 2) {
                                //sendStr += "\r\n\r\n#bAran:#k\r\n";
                                for (var i = 65; i < jobs.length; i++) {
                                        sendStr += "#L" + i + "#" + ids[i] + " - " + jobs[i] + "#l\r\n";
                                }
                        }
                        else if (selection == 3) {
                                //sendStr += "\r\n\r\n#bOthers:#k\r\n";
                                for (var i = 42; i < 44; i++) {
                                        sendStr += "#L" + i + "#" + ids[i] + " - " + jobs[i] + "#l\r\n";
                                }
                        }

                        cm.sendSimple(sendStr);
                } 
                else if (status == 2) {
                        var sendStr = "Use the command below to change your job and level:\r\n#b#L0#!c #r<Job ID Number>#b <Level>#k#l\r\n#b";
                        cm.sendPrev(sendStr);
                }
                else {
                        cm.dispose();
                }
        }
}
