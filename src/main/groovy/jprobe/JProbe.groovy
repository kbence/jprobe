/**
 *            DO WHAT THE FUCK YOU WANT TO PUBLIC LICENSE
 *                    Version 2, December 2004
 *
 * Copyright (C) 2004 Sam Hocevar <sam@hocevar.net>
 *
 * Everyone is permitted to copy and distribute verbatim or modified
 * copies of this license document, and changing it is allowed as long
 * as the name is changed.
 *
 *            DO WHAT THE FUCK YOU WANT TO PUBLIC LICENSE
 *   TERMS AND CONDITIONS FOR COPYING, DISTRIBUTION AND MODIFICATION
 *
 *  0. You just DO WHAT THE FUCK YOU WANT TO.
 */

package jprobe

import jprobe.torrent.provider.ncore.NCoreProvider

import java.util.logging.Logger

class JProbe {
    private static Logger logger = Logger.getLogger(JProbe.class.name)

    public static void main(String[] args) {
        def probe = new JProbe()
        probe.run()
    }

    public void run() {
        if (config.containsKey("providers")) {
            if (config.providers.containsKey("ncore")) {
                new NCoreProvider((ConfigObject) config.providers.ncore).search("something")
            }
        }
    }

    private ConfigObject getConfig() {
        def configFile = new File("${System.getenv("HOME")}/.jprobe.properties")

        if (configFile.isFile() && configFile.canRead()) {
            return new ConfigSlurper().parse(configFile.toURI().toURL())
        }

        new ConfigObject()
    }
}
