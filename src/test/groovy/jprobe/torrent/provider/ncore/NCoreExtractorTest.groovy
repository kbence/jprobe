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

package jprobe.torrent.provider.ncore

import groovy.util.slurpersupport.GPathResult
import groovy.util.slurpersupport.NodeChild
import org.cyberneko.html.parsers.SAXParser
import spock.lang.Specification

class NCoreExtractorTest extends Specification {
    def GPathResult getResourceHtml(String fileName) {
        def slurper = new XmlSlurper(new SAXParser())
        slurper.parseText(this.class.getResource(fileName).text)
    }

    def 'extractTorrent() returns the proper torrent objects'() {
        setup:
        def extractor = new NCoreExtractor(new ConfigObject())

        when:
        def result = extractor.extract((NodeChild)getResourceHtml('/test/data/ncore_search.html'))

        then:
        result.size() == 25
        result[0].name == 'test series'
        result[0].torrentUrl == 'https://ncore.cc/torrents.php?action=download&id=12345'
    }
}
