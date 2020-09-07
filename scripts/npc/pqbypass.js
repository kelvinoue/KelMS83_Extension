/**
 * @author Kelvinjang
 * Info: Party Quest Stage Bypasser
 * Script: pqbypass.js
*/

var status;
var eim;

function clearStageK(stage, eim, curMap) {
        eim.setProperty(stage + "stageclear", "true");
        eim.showClearEffect(true);
        eim.linkToNextStage(stage, "kpq", curMap);
}

function clearStageL(stage, eim, curMap) {
        eim.setProperty(stage + "stageclear", "true");
        eim.showClearEffect(true);
        eim.linkToNextStage(stage, "lpq", curMap);
}

function clearStageO(stage, eim) {
        eim.setProperty("statusStg" + stage, "1");
        eim.showClearEffect(true);
}

function start() {
        status = -1;
        action(1, 0, 0);
}

function action(mode, type, selection) {
        var curMap = cm.getMapId();
        var stage;
        eim = cm.getEventInstance();

        //Kerning PQ
        if(curMap >= 103000800 && curMap <= 103000804) {
                stage = curMap - 103000800 + 1
                if(eim.getProperty(stage.toString() + "stageclear") != null) {
                        if(stage < 5) {
                                cm.sendNext("Please proceed to the next stage. The portal has opened!");
                                cm.dispose();
                        }
                }
                else if(curMap == 103000804) {
                        clearStageK(stage, eim, curMap);
                        eim.clearPQ();
                        cm.dispose();
                }
                else {
                        clearStageK(stage, eim, curMap);
                        cm.dispose();
                }
        }

        //Ludi PQ
        else if(curMap == 922010100) { //S1 - Ratz
                stage = 1;
                clearStageL(stage, eim, curMap);
                cm.dispose();
        }
        else if(curMap == 922010200) { //S2 - Tele boxes
                stage = 2;
                clearStageL(stage, eim, curMap);
                cm.dispose();
        }
        else if(curMap == 922010300) { //S3 - Octo boxes
                stage = 3;
                clearStageL(stage, eim, curMap);
                cm.dispose();
        }
        else if(curMap == 922010400) { //S4 - Darkness in the tower
                stage = 4;
                clearStageL(stage, eim, curMap);
                cm.dispose();
        }
        else if(curMap == 922010500) { //S5 - Jump rooms
                stage = 5;
                clearStageL(stage, eim, curMap);
                cm.dispose();
        }
        else if(curMap == 922010600) { //S6 - 133 221 boxes
                stage = 6;
                clearStageL(stage, eim, curMap);
                eim.warpEventTeamToMapSpawnPoint(922010700, 0);
                cm.dispose();
        }
        else if(curMap == 922010700) { //S7 - Golems
                stage = 7;
                clearStageL(stage, eim, curMap);
                cm.dispose();
        }
        else if(curMap == 922010800) { //S8 - 12345 boxes
                stage = 8;
                clearStageL(stage, eim, curMap);
                cm.dispose();
        }
        else if(curMap == 922010900) { //S9 - Final Stage
                stage = 9;
                clearStageL(stage, eim, curMap);
                eim.clearPQ();
                cm.dispose();
        }

        //Orbis PQ
        else if(curMap == 920010000) { //S0 - Entrance
                stage = 0;
                eim.warpEventTeamToMapSpawnPoint(920010000, 2);
                clearStageO(stage, eim);
                cm.dispose();
        }
        else if(curMap == 920010100) { //S6 - Center Tower //ReactorID: 2006001
                if (eim.getIntProperty("statusStg7") == -1) {
                        for(var i = 1; i <= 6; i++) {
                                eim.setProperty("statusStg" + i, "1");
                                cm.getMap().getReactorByName("scar" + i).setState(1);
                            }
                        eim.warpEventTeam(920010800);
                }
                else if (eim.getIntProperty("statusStg7") == 1 && eim.getIntProperty("statusStg8") != 1) {
                        //cm.getMap().getReactorById(2006001).setState(1);
                        eim.setProperty("statusStg8", "1");
                        eim.clearPQ();
                        eim.showClearEffect(true);
                        cm.warp(920011300, 0);
                }
                cm.dispose();
        }
        else if(curMap == 920010800) { //S7 - Garden
                stage = 7;
                if (cm.canHold(4001055) && !cm.haveItem(4001055, 1)) {
                        cm.gainItem(4001055, 1); //Grass of Life
                        clearStageO(stage, eim);
                }
                cm.dispose();
        }
        else if(curMap == 920010200) { //S1 - Walkway
                stage = 1;
                if (cm.canHold(4001044) && !cm.haveItem(4001044, 1)) {
                        cm.gainItem(4001044, 1); //First piece
                        clearStageO(stage, eim);
                }
                cm.dispose();
        }
        else if(curMap == 920010300) { //S2 - Storage
                stage = 2;
                if (cm.canHold(4001045) && !cm.haveItem(4001045, 1)) {
                        cm.gainItem(4001045, 1); //Second piece
                        clearStageO(stage, eim);
                }
                cm.dispose();
        }
        else if(curMap == 920010400) { //S3 - Lobby
                stage = 3;
                if (cm.canHold(4001046) && !cm.haveItem(4001046, 1)) {
                        cm.gainItem(4001046, 1); //Third piece
                        clearStageO(stage, eim);
                }
                cm.dispose();
        }
        else if(curMap == 920010500) { //S4 - Sealed room
                stage = 4;
                if (cm.canHold(4001047) && !cm.haveItem(4001047, 1)) {
                        cm.gainItem(4001047, 1); //Fourth piece
                        clearStageO(stage, eim);
                }
                cm.dispose();
        }
        else if(curMap == 920010600) { //S5 - Lounge
                stage = 5;
                if (cm.canHold(4001048) && !cm.haveItem(4001048, 1)) {
                        cm.gainItem(4001048, 1); //Fifth piece
                        clearStageO(stage, eim);
                }
                cm.dispose();
        }
        else if(curMap == 920010700) { //S6 - On the way up
                stage = 6;
                if (cm.canHold(4001049) && !cm.haveItem(4001049, 1)) {
                        cm.gainItem(4001049, 1); //Sixth piece
                        clearStageO(stage, eim);
                }
                cm.dispose();
        }

        else {
                cm.dispose();
        }
}