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
import groovyx.net.http.HTTPBuilder
import groovyx.net.http.HttpResponseDecorator
import groovyx.net.http.HttpResponseException
import jprobe.torrent.Torrent
import jprobe.torrent.provider.AbstractProvider

class NCoreProvider extends AbstractProvider {
    private boolean logged = false
    private String username
    private String password

    private NCoreExtractor extractor = new NCoreExtractor()

    public NCoreProvider(ConfigObject config) {
        username = config.username
        password = config.password
    }

    @Override
    public List<Torrent> search(String keyword) {
        List<Torrent> torrents = []

        try {
            ensureLoggedIn()

            def query = [mire : keyword,
                         miben: 'name',
                         tipus: 'all_own']

            get(path: '/torrents.php', query: query) { HttpResponseDecorator resp, NodeChild html ->
                torrents = extractor.extract(html)
            }
        } catch (HttpResponseException e) {
            e.printStackTrace()
        }

        return torrents
    }

    private void ensureLoggedIn() {
        if (!logged) {
            def body = [
                    nev      : username,
                    pass     : password,
                    submitted: 1,
                    set_lang : 'en'
            ]

            post(path: '/login.php', body: body) { HttpResponseDecorator resp, html -> }
        }
    }

    @Override
    protected HTTPBuilder getBuilder() {
        def builder = super.getBuilder()
        builder.uri = "https://ncore.cc"
        return builder
    }
}
