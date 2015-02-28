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

import groovy.util.logging.Slf4j
import jprobe.model.Episode
import jprobe.model.Show
import jprobe.torrent.EpisodeFinder
import jprobe.torrent.provider.ProviderFactory

@Slf4j
class JProbe {
    def providerFactory = new ProviderFactory()
    def episodeFinder = new EpisodeFinder()

    public void run() {
        log.info("JProbe is starting up...")

        if (config.containsKey("providers")) {
            log.info("Searching for providers in config")

           config.providers.each { String name, ConfigObject config ->
               log.info("Adding provider '$name'")

               episodeFinder.addProvider(providerFactory.create(name, config))
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

    public static void main(String[] args) {
        def probe = new JProbe()
        probe.run()
    }
}
