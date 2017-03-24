;(function(window) {

  var svgSprite = '<svg>' +
    '' +
    '<symbol id="icon-circularframeshijian" viewBox="0 0 1024 1024">' +
    '' +
    '<path d="M512 32C246.848 32 32 246.848 32 512s214.848 480 480 480 480-214.848 480-480S777.152 32 512 32zM674.432 674.432c-11.712 11.712-27.072 17.536-42.432 17.536s-30.72-5.888-42.432-17.536L469.632 554.496c-10.88-10.752-17.6-25.728-17.6-42.304L452.032 272c0-33.216 26.816-60.032 60.032-60.032 33.216 0 60.032 26.816 60.032 60.032l0 215.168 102.464 102.4C697.856 612.992 697.856 651.008 674.432 674.432z"  ></path>' +
    '' +
    '</symbol>' +
    '' +
    '<symbol id="icon-iconziti09" viewBox="0 0 1024 1024">' +
    '' +
    '<path d="M883.823935 603.00569l-46.921659 0c-7.612375 27.612894-18.546402 53.772694-32.525788 78.062913l43.35646 43.322691c27.752064 27.751041 27.752064 72.734558 0 100.484575l-25.121144 25.122167c-27.751041 27.752064-72.734558 27.752064-100.485599 0l-43.633776-43.633776c-24.187889 13.770632-50.278103 24.534789-77.750805 32.006971l0 45.50131c0 39.241746-31.800263 71.039963-71.039963 71.039963l-35.501562 0c-39.239699 0-71.073732-31.798217-71.073732-71.039963l0-45.50131c-27.438932-7.473205-53.530171-18.23634-77.751828-32.006971l-43.6348 43.633776c-27.715225 27.752064-72.698742 27.752064-100.449783 0l-25.122167-25.122167c-27.751041-27.750017-27.751041-72.733535 0-100.484575l43.357484-43.322691c-13.979386-24.290219-24.914436-50.450019-32.56058-78.062913l-46.886867 0c-39.239699 0-71.039963-31.800263-71.039963-71.004147l0-35.538401c0-39.239699 31.800263-71.040986 71.039963-71.040986l46.332235 0c7.336082-27.334555 17.85567-53.356208 31.454386-77.507258l-41.696658-41.662889c-27.751041-27.748994-27.751041-72.732511 0-100.484575l25.122167-25.120121c27.751041-27.754111 72.734558-27.754111 100.449783 0l40.866756 40.899502c25.01779-14.56881 52.007491-25.917276 80.520895-33.703613l0-47.716768c0-39.242769 31.834033-71.039963 71.073732-71.039963l35.501562 0c39.239699 0 71.039963 31.797194 71.039963 71.039963l0 47.716768c28.54615 7.785314 55.535851 19.133779 80.519872 33.703613l40.865733-40.899502c27.751041-27.754111 72.734558-27.754111 100.485599 0l25.121144 25.120121c27.752064 27.752064 27.752064 72.735581 0 100.484575l-41.695634 41.662889c13.598716 24.152073 24.083511 50.172703 31.453363 77.507258l46.333258 0c39.239699 0 71.037916 31.801287 71.037916 71.040986l0 35.538401C954.861852 571.205427 923.062612 603.00569 883.823935 603.00569zM511.948323 319.956126c-107.890242 0-195.364503 87.475284-195.364503 195.366549 0 107.890242 87.47426 195.364503 195.364503 195.364503 107.892289 0 195.366549-87.47426 195.366549-195.364503C707.314873 407.432433 619.841635 319.956126 511.948323 319.956126zM511.948323 601.896426c-49.030693 0-88.824001-39.721677-88.824001-88.788186 0-49.068556 39.793308-88.790232 88.824001-88.790232 49.03274 0 88.792279 39.720653 88.792279 88.790232C600.740602 562.173726 560.981063 601.896426 511.948323 601.896426z"  ></path>' +
    '' +
    '</symbol>' +
    '' +
    '<symbol id="icon-tuichu" viewBox="0 0 1024 1024">' +
    '' +
    '<path d="M711.036126 231.964072l-87.387279 124.830053c64.309678 38.303374 107.403149 108.516506 107.403149 188.804087 0 121.285321-98.323353 219.609697-219.61072 219.609697S291.830555 666.883533 291.830555 545.598212c0-80.271208 43.075051-150.47206 107.362216-188.780551L311.804469 231.986585c-103.46649 66.00018-172.086333 181.792393-172.086333 313.615721 0 205.294666 166.42438 371.719046 371.721093 371.719046 205.294666 0 371.721093-166.425404 371.721093-371.719046C883.159298 413.763628 814.523081 297.960159 711.036126 231.964072zM511.600911 612.744495c35.397185 0 64.091714-28.982078 64.091714-64.730257L575.692624 160.814616c0-35.748179-28.694529-64.730257-64.091714-64.730257s-64.091714 28.982078-64.091714 64.730257l0 387.199623C447.51022 583.762417 476.203726 612.744495 511.600911 612.744495z"  ></path>' +
    '' +
    '</symbol>' +
    '' +
    '</svg>'
  var script = function() {
    var scripts = document.getElementsByTagName('script')
    return scripts[scripts.length - 1]
  }()
  var shouldInjectCss = script.getAttribute("data-injectcss")

  /**
   * document ready
   */
  var ready = function(fn) {
    if (document.addEventListener) {
      if (~["complete", "loaded", "interactive"].indexOf(document.readyState)) {
        setTimeout(fn, 0)
      } else {
        var loadFn = function() {
          document.removeEventListener("DOMContentLoaded", loadFn, false)
          fn()
        }
        document.addEventListener("DOMContentLoaded", loadFn, false)
      }
    } else if (document.attachEvent) {
      IEContentLoaded(window, fn)
    }

    function IEContentLoaded(w, fn) {
      var d = w.document,
        done = false,
        // only fire once
        init = function() {
          if (!done) {
            done = true
            fn()
          }
        }
        // polling for no errors
      var polling = function() {
        try {
          // throws errors until after ondocumentready
          d.documentElement.doScroll('left')
        } catch (e) {
          setTimeout(polling, 50)
          return
        }
        // no errors, fire

        init()
      };

      polling()
        // trying to always fire before onload
      d.onreadystatechange = function() {
        if (d.readyState == 'complete') {
          d.onreadystatechange = null
          init()
        }
      }
    }
  }

  /**
   * Insert el before target
   *
   * @param {Element} el
   * @param {Element} target
   */

  var before = function(el, target) {
    target.parentNode.insertBefore(el, target)
  }

  /**
   * Prepend el to target
   *
   * @param {Element} el
   * @param {Element} target
   */

  var prepend = function(el, target) {
    if (target.firstChild) {
      before(el, target.firstChild)
    } else {
      target.appendChild(el)
    }
  }

  function appendSvg() {
    var div, svg

    div = document.createElement('div')
    div.innerHTML = svgSprite
    svgSprite = null
    svg = div.getElementsByTagName('svg')[0]
    if (svg) {
      svg.setAttribute('aria-hidden', 'true')
      svg.style.position = 'absolute'
      svg.style.width = 0
      svg.style.height = 0
      svg.style.overflow = 'hidden'
      prepend(svg, document.body)
    }
  }

  if (shouldInjectCss && !window.__iconfont__svg__cssinject__) {
    window.__iconfont__svg__cssinject__ = true
    try {
      document.write("<style>.svgfont {display: inline-block;width: 1em;height: 1em;fill: currentColor;vertical-align: -0.1em;font-size:16px;}</style>");
    } catch (e) {
      console && console.log(e)
    }
  }

  ready(appendSvg)


})(window)