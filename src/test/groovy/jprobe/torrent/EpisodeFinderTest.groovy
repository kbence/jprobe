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

import jprobe.model.Episode
import jprobe.model.Show
import jprobe.torrent.provider.Provider
import spock.lang.Specification

class EpisodeFinderTest extends Specification {
    private EpisodeFinder finder

    void setup() {
        finder = new EpisodeFinder()
    }

    def "search() should search in all providers"() {
        setup:
        def provider1 = Mock(Provider)
        def provider2 = Mock(Provider)
        def result1 = [new Torrent(), new Torrent()]
        def result2 = [new Torrent()]

        finder.addProvider(provider1)
        finder.addProvider(provider2)

        when:
        def result = finder.search(new Show("Test Show Name"), new Episode(5, 6))

        then:
        result == result1 + result2
        1 * provider1.search("Test Show Name S05E06") >> result1
        1 * provider2.search("Test Show Name S05E06") >> result2
    }
}
