$(function() {
	handleInputBoxChange();
	initSubmitListener();
	initRetryButton();
	initDownloadButtonListener();

	/* ---------------- */

	function handleInputBoxChange() {
		$('#form-text').on('input', function() {
			$('.result').hide();
		});
	}

	function getDownloadUrl(url) {
		$('form input').attr('disabled', 'disabled');
		$('#result-fail').hide();
		$('#result-processing').show();

		var postData = {
			url: url
		};

		var t0 = 0;
		try {
			t0 = performance.now();
		} catch(e) {
			// no-op
		}

		$.ajax({
			type: "POST",
			url: "/v1/drama/fetchstreams",
			data: JSON.stringify(postData),
			success: function(response) {
				var elapsedMsec = 0;
				try {
					var t1 = performance.now();
					elapsedMsec = Math.round(t1 - t0);
				} catch(e) {
					// no-op
				}

				if(response.status == "OK") {
					ga('send', 'event', 'downloads', 'submit-success', url, elapsedMsec);
					handleSuccess(response);
				} else if(response.status == "FAILED") {
					ga('send', 'event', 'downloads', 'submit-fail-int', url, elapsedMsec);
					handleFail(url);
				} else {
					ga('send', 'event', 'downloads', 'submit-unsupported', url, elapsedMsec);
					handleFail();
				}
			},
			error: function() {
				try {
					var t1 = performance.now();
					var elapsedMsec = Math.round(t1 - t0);
					ga('send', 'event', 'downloads', 'submit-fail-ext', url, elapsedMsec);
				} catch(e) {
					// no-op
				}

				handleFail();
			}
		});
	}

	function initSubmitListener() {
		$('#form-main').submit(function(e) {
		  e.preventDefault();

		  $(".result").hide();
		  clearDownloadButtons();

			var origUrl = $('#form-text').val();
			var trimUrl = origUrl.trim();
			$('#form-text').val(trimUrl);
			ga('send', 'event', 'downloads', 'submit', trimUrl);
			getDownloadUrl(trimUrl);
		});
	}

	function initRetryButton() {
		$('a.button-retry').click(function() {
			var url = $('#form-text').val();
			getDownloadUrl(url);
		});
	}

	function initDownloadButtonListener() {
		$(document).on('click', '.button-download', function(e) {
			ga('send', 'event', 'downloads', 'download-lclick');
		});

		$(document).on('contextmenu', '.button-download', function(e) {
			ga('send', 'event', 'downloads', 'download-rclick');
		});
	}

	function createDownloadButtons(downloadables) {
		var caption = "DOWNLOAD";
		for(var i = 0; i < downloadables.length; i++) {
			var buttonText = caption + ' <br />(' + downloadables[i].name + ')';
			var $button = $('<a>', {
				class   : 'button button-download',
				href    : downloadables[i].url,
			}).html(buttonText);

			if(downloadables[i].isDirectLink)
				$button.attr("download", "");
			else
				$button.attr("target", "_blank");

			$('#download-links').append($button);
		}
	}

	function clearDownloadButtons() {
	  $('#download-links').empty();
	}

	function handleSuccess(response) {
		setTimeout(function() {
			$('form input').removeAttr('disabled', 'disabled');
			$('.result').hide();
			$('#result-success').show();

			if(response.title) {
				$("#download-title").text(response.title);
				document.title = response.title + " - DramaDownloader.com";
			}

			createDownloadButtons(response.links);
		}, 2000);
	}

	function handleFail(url) {
		$('form input').removeAttr('disabled', 'disabled');
		$('.result').hide();
		$('#result-fail').show();
		$('#origin-url').attr('href', url);
	}

	function handleUnsupported() {
		$('form input').removeAttr('disabled', 'disabled');
		$('.result').hide();
		$('#result-unsupported').show();
	}
});
