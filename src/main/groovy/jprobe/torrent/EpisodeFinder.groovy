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

package jprobe.torrent

import groovy.util.logging.Slf4j
import jprobe.model.Episode
import jprobe.model.Show
import jprobe.torrent.provider.Provider

@Slf4j
class EpisodeFinder {
    protected List<Provider> providers = []

    def addProvider(Provider provider) {
        providers << provider
    }

    def List<Torrent> search(Show show, Episode episode) {
        providers.collectMany { provider ->
            def searchString = "${show.name} ${sprintf("S%02dE%02d", episode.season, episode.episode)}"

            log.debug("Searching for '$searchString'")
            provider.search(searchString)
        }.toList()
    }
}
