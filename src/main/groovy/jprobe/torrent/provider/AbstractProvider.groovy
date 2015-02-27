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

package jprobe.torrent.provider

import groovyx.net.http.HTTPBuilder
import groovyx.net.http.HttpResponseDecorator
import org.apache.http.HttpRequest
import org.apache.http.HttpResponse
import org.apache.http.ProtocolException
import org.apache.http.impl.client.DefaultRedirectStrategy
import org.apache.http.protocol.HttpContext

abstract class AbstractProvider implements Provider {
    protected HTTPBuilder builder = null
    protected Map<String, String> cookies = new HashMap<>()

    protected HTTPBuilder getBuilder() {
        if (!builder) {
            builder = new HTTPBuilder()
            builder.client.redirectStrategy = new DefaultRedirectStrategy() {
                @Override
                boolean isRedirected(HttpRequest request, HttpResponse response, HttpContext context) throws ProtocolException {
                    return super.isRedirected(request, response, context) || response.statusLine.statusCode == 302
                }
            }
        }

        return builder
    }

    protected Object get(Map<String, ?> params, Closure closure) {
        def builder = getBuilder()
        prepareBuilder(builder)

        def result = builder.get(params) { HttpResponseDecorator resp, result ->
            processResponse(resp)
            closure(resp, result)
        }

        return result
    }

    protected Object post(Map<String, ?> params, Closure closure) {
        def builder = getBuilder()
        prepareBuilder(builder)

        def result = builder.post(params) { HttpResponseDecorator resp, result ->
            processResponse(resp)
            closure(resp, result)
        }

        return result
    }

    public void prepareBuilder(HTTPBuilder builder) {
        builder.headers['Cookie'] = cookies.collect { name, value -> "$name=$value" }.join('; ')
    }

    public void processResponse(HttpResponseDecorator response) {
        response.getHeaders('Set-Cookie').each { value ->
            def cookie = value.toString().split(';')[0]
            cookies[cookie.split('=')[0]] = cookie.split('=')[1]
        }
    }
}
