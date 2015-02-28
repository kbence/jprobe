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

import groovy.util.slurpersupport.NodeChild
import jprobe.torrent.Torrent

class NCoreExtractor {
    public List<Torrent> extract(NodeChild html) {
        html.'**'.findAll {
            it.@class.toString().split('\\s+').contains('box_torrent')
        }.collect { NodeChild node ->
            def hit = new Torrent()
            def torrentId = extractId((String)(node.'**'.find {
                    it.@href.text().contains('&id=')
                }.@href.text()))

            hit.name = node.DIV[1].DIV[0].text().toString()
            hit.torrentUrl = "https://ncore.cc/torrents.php?action=download&id=$torrentId"
            hit
        }.toList()
    }

    private int extractId(String url) {
        url.split('\\?')[1].split('&').find { it.split('=')[0] == 'id' }.split('=')[1].toInteger()
    }
}
