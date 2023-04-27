/**
* 
*/

window.onload = function() {
	if (window.history && window.history.pushState) {
		window.history.pushState('', null, '');
		window.onpopstate = function() {
			window.history.pushState('', null, '');
		};
	}
};
$(window).on('load pageshow', function() { // add pageshow event listener

	var userEmail = $('#span_session').text();
	if (!userEmail || userEmail === '') {
		Swal.fire({
			title: 'No credential detected!',
			text: 'Need to re-login again!',
			icon: 'danger',
			confirmButtonText: 'OK',
			allowOutsideClick: false
		}).then((s) => {
			if (s.isConfirmed) {
				window.location.href = '/'; // replace with your login page URL
			}
		});
	}
});

$(document).ready(function() {

	// Attach click event handler to the parent of the delete buttons using event delegation
	$('#pending-table').on('click', '.delete_btn_client', function(event) {
		event.preventDefault();
		deleteUserTicket($(this));
	});

	function deleteUserTicket(button) {
		var tableId = "pending-table";
		var ticketID = button.closest('tr').find('td:eq(0)').text();

		// Show loading animation with Swal
		Swal.fire({
			title: 'Are you sure?',
			text: 'You will not be able to revert this!',
			icon: 'warning',
			showCancelButton: true,
			confirmButtonColor: '#d33',
			cancelButtonColor: '#3085d6',
			confirmButtonText: 'Delete',
			cancelButtonText: 'Cancel'
		}).then((result) => {
			if (result.isConfirmed) {
				var xhr = new XMLHttpRequest();

				xhr.open('DELETE', '/tickets/delete/' + ticketID);

				xhr.onreadystatechange = function() {
					if (this.readyState === XMLHttpRequest.DONE) {
						if (this.status === 200) {
							// Show a success message
							Swal.fire({
								icon: 'success',
								title: 'Success',
								text: 'Ticket Deleted successfully!'
							}).then((result) => {
								if (result.isConfirmed) {
									// Reload the data
									$('#' + tableId).load('/updateClientTicket?tableId=' + tableId + ' #' + tableId + ' > *', function() {
										// callback function
										// callback function to update the value of the pending count span
										var pendingCount = parseInt($('#pending_count').text());
										$('#pending_count').text(pendingCount - 1);


										$(document).ready(function() {
											$('.eBtn').on('click', function(event) {

												event.preventDefault();

												var ticketId = $(this).closest('tr').find('td:eq(0)').text();
												var dateCreated = $(this).closest('tr').find('td:eq(4)').text();
												var title = $(this).closest('tr').find('td:eq(2)').text();
												var description = $(this).closest('tr').find('td:eq(3)').text();
												var userName = $(this).closest('tr').find('td:eq(1)').text();
												var status = $(this).closest('tr').find('td:eq(5)').text();
												var conforme_no = $(this).closest('tr').find('td:eq(6)').text();
												$('#id').val(ticketId);
												$('#date').val(dateCreated);
												$('#title').val(title);
												$('#description').val(description);
												$('#name').val(userName);
												$('#status').val(status);
												$('#conforme_no').val(conforme_no);
												$('#viewTicket').modal();


											});
										});
									});
								}
							});
						} else {
							// Show an error message
							Swal.fire({
								icon: 'error',
								title: 'Error',
								text: 'There was an error while deleting the ticket: ' + this.statusText
							});
						}
					}
				};

				xhr.send();
			}
		});
	}
});







//submit signature ongoing

$(document).ready(function() {
	$('.submit_signature_btn').on('click', function() {

		var ticketID = $('#ticket_id_ongoing').val();
		console.log(ticketID);
		var form = document.getElementById("conforme-update-signature");
		var formData = new FormData(form);
		var xhr = new XMLHttpRequest();
		var url = "/tickets/update-tickets-client/" + ticketID;
		var tableId = "ongoing-table";
		// Show loading animation with Swal
		Swal.fire({
			title: 'Loading...',
			allowEscapeKey: false,
			allowOutsideClick: false,
			didOpen: () => {
				Swal.showLoading();
			}
		});

		xhr.open("PUT", url);
		xhr.onload = function() {
			if (xhr.status === 200) {
				// Show a success message
				console.log("Ticket updated successfully");

				Swal.fire({
					icon: 'success',
					title: 'Success',
					text: 'Ticket updated successfully!',
				}).then((result) => {
					if (result.isConfirmed) {

						$('#conformeTicket').modal('hide');

						$('#' + tableId).load('/updateClientTicket?tableId=' + tableId + ' #' + tableId + ' > *', function() {
							// callback function
							// callback function to update the value of the pending count span
							var ongoing_count = parseInt($('#ongoing_count').text());
							$('#ongoing_count').text(ongoing_count + 1);

							$(document).ready(function() {
								$('.ongoing').on('click', function() {
									//to get the data in the table row
									var ticketId = $(this).closest('tr').find('td:eq(0)').text();
									var dateCreated = $(this).closest('tr').find('td:eq(4)').text();
									var title = $(this).closest('tr').find('td:eq(2)').text();
									var description = $(this).closest('tr').find('td:eq(3)').text();
									var userName = $(this).closest('tr').find('td:eq(1)').text();
									var progress = $(this).closest('tr').find('td:eq(5)').text();
									var status = $(this).closest('tr').find('td:eq(6)').text();
									var conforme_no = $(this).closest('tr').find('td:eq(7)').text();
									var amount = $(this).closest('tr').find('td:eq(8)').text();
									var signature = $(this).closest('tr').find('td:eq(9)').text();
									var conforme_date_ongoing = $(this).closest('tr').find('td:eq(10)').text();


									//setting the value from the inputs
									$('#ticket_id_ongoing').val(ticketId);
									$('#date').val(dateCreated);
									$('#title_ongoing_').val(title);
									$('#description_ongoing_').val(description);
									$('#user_name').val(userName);
									$('#progress').val(progress);
									$('#status_ongoing').val(status);
									$('#conforme_no').val(conforme_no);
									$('#amount').val(amount);
									$('#thumbnail_ongoing').attr('src', '/img/' + signature);
									$('#_conforme_date_ongoing').val(conforme_date_ongoing);
									$('#salesSignature').val(signature);

									var client_signature_ongoing = $(this).closest('tr').find('td:eq(11)').text();
									var client_payment_proof_ongoing = $(this).closest('tr').find('td:eq(12)').text();

									if (client_signature_ongoing && client_payment_proof_ongoing) {
										// Both fields have values, hide ClientDatas and show ClientDataRetrieved
										$("#submit_button_ongoing").hide();
										$("#ClientDatas").hide();
										$("#view_status").show();
										$("#ClientDataRetrieved").show();
										//for images
										$('#client_payment_proof_ongoing').attr('src', '/img/' + client_payment_proof_ongoing);
										$('#client_signature_ongoing').attr('src', '/img/' + client_signature_ongoing);
									} else {
										// At least one field is null, show ClientDatas and hide ClientDataRetrieved
										$("#submit_button_ongoing").show();
										$("#view_status").hide();

										$("#ClientDatas").show();
										$("#ClientDataRetrieved").hide();

									}
								});
							});


							// Event listener for On-going Tickets button
							$('.menu-item[data-table="ongoing"]').on('click', function() {
								// Show all rows
								$('#ongoing-table tbody tr').show();

								// Remove filters from other buttons
								$('.waiting_button').removeClass('active');
								$('.ready_to_proceed').removeClass('active');
								$('.ready_to_resolved').removeClass('active');
							});

							// Event listener for Waiting List button
							$('.waiting_button').on('click', function() {
								// Loop through each table row
								$('#ongoing-table tbody tr').each(function() {
									var signature = $(this).find('td:nth-child(11)').text();
									var paymentProof = $(this).find('td:nth-child(12)').text();
									// If either signature or payment proof is null, hide the row
									if (signature && paymentProof) {
										$(this).hide();
									} else {
										$(this).show();
									}
								});
							});

							// Event listener for Ready to proceed button
							$('.ready_to_proceed').on('click', function() {
								// Loop through each table row
								$('#ongoing-table tbody tr').each(function() {
									var _status = $(this).find('td:nth-child(5)').text();
									var signature = $(this).find('td:nth-child(11)').text();
									var paymentProof = $(this).find('td:nth-child(12)').text();
									console.log(_status);
									if ((!signature || !paymentProof) || (_status === 'closed')) {
										$(this).hide();
									} else {
										$(this).show();
									}
								});
							});

							$(document).ready(function() {
								$('.imageProof').on('click', function() {
									var image_proof = $("#client_payment_proof").attr("src");


									$('#imageViewerImage').attr('src', image_proof);

									$('#imageViewerModal').modal();

								});

							});

							$(document).ready(function() {
								$('.imageSignature').on('click', function() {
									var image_proof = $("#client_signature").attr("src");
									$('#imageViewerImage').attr('src', image_proof);
									$('#imageViewerModal').modal();

								});
							});



							$(document).ready(function() {
								$('#view_status').on('click', function() {
									var ticketId = $('#ticket_id_ongoing').val(); // replace with the ticket ID that you want to use
									var url = '/tickets/' + ticketId; // replace with the URL pattern that you want to use
									window.location.href = url; // redirect to the new URL
								});
							});

						});



					} else {
						// Show error message with SweetAlert
						Swal.fire({
							icon: 'error',
							title: 'Error',
							text: 'There was an error while saving the user update: ' + xhr.statusText,
						});
					}
				});

			} else {
				// Show an error message
				console.log("Error Updating Invoice");
			}
		};
		xhr.send(formData);

	});
});

$(document).ready(function() {
	$('.create_ticket_button').on('click', function(event) {
		event.preventDefault();

		var createTicketform = $('#create-ticket-form')[0];
		var formData = new FormData(createTicketform);

		var tableId = "pending-table";

		Swal.fire({
			title: 'Loading...',
			allowEscapeKey: false,
			allowOutsideClick: false,
			didOpen: () => {
				Swal.showLoading();
			}
		});

		$.ajax({
			type: "POST",
			url: "/tickets/post-ticket",
			data: formData,
			contentType: false,
			processData: false,
			success: function(data) {
				Swal.fire({
					icon: 'success',
					title: 'Success',
					text: 'Ticket created successfully!',
				}).then((result) => {
					if (result.isConfirmed) {
						$('#createTicketModal').modal('hide');

						$('#' + tableId).load('/updateClientTicket?tableId=' + tableId + ' #' + tableId + ' > *', function() {
							var pendingCount = parseInt($('#pending_count').text());
							$('#pending_count').text(pendingCount + 1);


							$(document).ready(function() {
								$('.eBtn').on('click', function(event) {

									event.preventDefault();

									var ticketId = $(this).closest('tr').find('td:eq(0)').text();
									var dateCreated = $(this).closest('tr').find('td:eq(4)').text();
									var title = $(this).closest('tr').find('td:eq(2)').text();
									var description = $(this).closest('tr').find('td:eq(3)').text();
									var userName = $(this).closest('tr').find('td:eq(1)').text();
									var status = $(this).closest('tr').find('td:eq(5)').text();
									var conforme_no = $(this).closest('tr').find('td:eq(6)').text();
									$('#id').val(ticketId);
									$('#date').val(dateCreated);
									$('#title').val(title);
									$('#description').val(description);
									$('#name').val(userName);
									$('#status').val(status);
									$('#conforme_no').val(conforme_no);
									$('#viewTicket').modal();


								});
							});
						});
					}
				});
			},
			error: function(xhr, status, error) {
				Swal.fire({
					icon: 'error',
					title: 'Error',
					text: 'There was an error while creating the ticket: ' + error,
				});
			}
		});
	});
});
//Button for ongoing 
$(document).ready(function() {

	$('.completed_modal').on('click', function(event) {
		var ticketId = $(this).closest('tr').find('td:eq(0)').text();
		var dateCreated = $(this).closest('tr').find('td:eq(1)').text();
		var title = $(this).closest('tr').find('td:eq(2)').text();
		var userName = $(this).closest('tr').find('td:eq(3)').text();
		var status = $(this).closest('tr').find('td:eq(4)').text();
		var amount = $(this).closest('tr').find('td:eq(5)').text();
		var conformeNo = $(this).closest('tr').find('td:eq(6)').text(); // retrieve the conforme_no value from the button's data-conforme-no attribute
		var progress = $(this).closest('tr').find('td:eq(7)').text();
		var conforme_date_ongoing = $(this).closest('tr').find('td:eq(8)').text();
		var signature = $(this).closest('tr').find('td:eq(9)').text();
		var description = $(this).closest('tr').find('td:eq(10)').text();
		var client_signature = $(this).closest('tr').find('td:eq(11)').text();
		var client_payment_proof = $(this).closest('tr').find('td:eq(12)').text();
		var toBilling = "billing_team";
		var toSupport = "support_team";

		console.log(status + ":status");
		console.log(progress + ":progress");
		$('#thumbnail').attr('src', '/img/' + signature);

		$('#client_payment_proof').attr('src', '/img/' + client_payment_proof);
		$('#client_signature').attr('src', '/img/' + client_signature);

		$('#client_payment_proofs').val(client_payment_proof);
		$('#client_signatures').val(client_signature);
		$('#salesSignatures').val(signature);

		if (progress === "completed") {
			$('#ticket_not_resolved').show();
		} else {
			$('#ticket_not_resolved').hide();
		}

		$('#ticket_id').val(ticketId);
		$('#conforme_date_ongoing').val(conforme_date_ongoing);
		$('#title_ongoing').val(title);
		$('#description_ongoing').val(description);
		$('#user_name_ongoing').val(userName);
		$('#status_').val(status);
		$('#amount_ongoing').val(amount);
		$('#conforme_no_ongoing').val(conformeNo); // set the conforme_no value in the #modalView modal
		$('#completedModal').modal();
	});
});



function handleClick(menuItem) {
	// Get selected menu item and corresponding table ID
	var selectedTableId = $(menuItem).data('table');

	// Remove active class from all menu items and add to selected item
	$('.menu-item').removeClass('active');
	$(menuItem).addClass('active');

	// Hide all tables and show selected table
	$('.table-data').hide();
	$('#' + selectedTableId + '-table').show();
}

$(document).ready(function() {
	$('.ongoing').on('click', function(event) {
		//to get the data in the table row
		var ticketId = $(this).closest('tr').find('td:eq(0)').text();
		var dateCreated = $(this).closest('tr').find('td:eq(4)').text();
		var title = $(this).closest('tr').find('td:eq(2)').text();
		var description = $(this).closest('tr').find('td:eq(3)').text();
		var userName = $(this).closest('tr').find('td:eq(1)').text();
		var progress = $(this).closest('tr').find('td:eq(5)').text();
		var status = $(this).closest('tr').find('td:eq(6)').text();
		var conforme_no = $(this).closest('tr').find('td:eq(7)').text();
		var amount = $(this).closest('tr').find('td:eq(8)').text();
		var signature = $(this).closest('tr').find('td:eq(9)').text();
		var conforme_date_ongoing = $(this).closest('tr').find('td:eq(10)').text();


		//setting the value from the inputs
		$('#ticket_id_ongoing').val(ticketId);
		$('#date').val(dateCreated);
		$('#title_ongoing_').val(title);
		$('#description_ongoing_').val(description);
		$('#user_name').val(userName);
		$('#progress').val(progress);
		$('#status_ongoing').val(status);
		$('#conforme_no').val(conforme_no);
		$('#amount').val(amount);
		$('#thumbnail_ongoing').attr('src', '/img/' + signature);
		$('#_conforme_date_ongoing').val(conforme_date_ongoing);
		$('#salesSignature').val(signature);

		var client_signature_ongoing = $(this).closest('tr').find('td:eq(11)').text();
		var client_payment_proof_ongoing = $(this).closest('tr').find('td:eq(12)').text();

		if (client_signature_ongoing && client_payment_proof_ongoing) {
			// Both fields have values, hide ClientDatas and show ClientDataRetrieved
			$("#submit_button_ongoing").hide();
			$("#ClientDatas").hide();
			$("#view_status").show();
			$("#ClientDataRetrieved").show();
			//for images
			$('#client_payment_proof_ongoing').attr('src', '/img/' + client_payment_proof_ongoing);
			$('#client_signature_ongoing').attr('src', '/img/' + client_signature_ongoing);
		} else {
			// At least one field is null, show ClientDatas and hide ClientDataRetrieved
			$("#submit_button_ongoing").show();
			$("#view_status").hide();

			$("#ClientDatas").show();
			$("#ClientDataRetrieved").hide();

		}
	});
});



$(document).ready(function() {
	$('.conforme_ongoing').on('click', function(event) {

		event.preventDefault();

		var ticketId = $(this).closest('tr').find('td:eq(0)').text();
		var dateCreated = $(this).closest('tr').find('td:eq(4)').text();
		var title = $(this).closest('tr').find('td:eq(2)').text();
		var description = $(this).closest('tr').find('td:eq(3)').text();
		var userName = $(this).closest('tr').find('td:eq(1)').text();
		var status = $(this).closest('tr').find('td:eq(5)').text();
		var conforme_no = $(this).closest('tr').find('td:eq(6)').text();
		$('#id').val(ticketId);
		$('#date').val(dateCreated);
		$('#title').val(title);
		$('#description').val(description);
		$('#name').val(userName);
		$('#status').val(status);
		$('#conforme_no').val(conforme_no);
		$('#conformeTicket').modal();


	});
});

$(document).ready(function() {
	$('.eBtn').on('click', function(event) {

		event.preventDefault();

		var ticketId = $(this).closest('tr').find('td:eq(0)').text();
		var dateCreated = $(this).closest('tr').find('td:eq(4)').text();
		var title = $(this).closest('tr').find('td:eq(2)').text();
		var description = $(this).closest('tr').find('td:eq(3)').text();
		var userName = $(this).closest('tr').find('td:eq(1)').text();
		var status = $(this).closest('tr').find('td:eq(5)').text();
		var conforme_no = $(this).closest('tr').find('td:eq(6)').text();
		$('#id').val(ticketId);
		$('#date').val(dateCreated);
		$('#title').val(title);
		$('#description').val(description);
		$('#name').val(userName);
		$('#status').val(status);
		$('#conforme_no').val(conforme_no);
		$('#viewTicket').modal();


	});
});

$(document).ready(function() {
	$('.imageProof').on('click', function() {
		var image_proof = $("#client_payment_proof").attr("src");


		$('#imageViewerImage').attr('src', image_proof);

		$('#imageViewerModal').modal();

	});

});

$(document).ready(function() {
	$('.imageSignature').on('click', function() {
		var image_proof = $("#client_signature").attr("src");
		$('#imageViewerImage').attr('src', image_proof);
		$('#imageViewerModal').modal();

	});
});



$(document).ready(function() {
	$('#view_status').on('click', function() {
		var ticketId = $('#ticket_id_ongoing').val(); // replace with the ticket ID that you want to use
		var url = '/tickets/' + ticketId; // replace with the URL pattern that you want to use
		window.location.href = url; // redirect to the new URL
	});
});




/*MENU BUTTONS FOR ONGOING TABLE*/

// Event listener for On-going Tickets button
$('.menu-item[data-table="ongoing"]').on('click', function() {
	// Show all rows
	$('#ongoing-table tbody tr').show();

	// Remove filters from other buttons
	$('.waiting_button').removeClass('active');
	$('.ready_to_proceed').removeClass('active');
	$('.ready_to_resolved').removeClass('active');
});

// Event listener for Waiting List button
$('.waiting_button').on('click', function() {
	// Loop through each table row
	$('#ongoing-table tbody tr').each(function() {
		var signature = $(this).find('td:nth-child(11)').text();
		var paymentProof = $(this).find('td:nth-child(12)').text();
		// If either signature or payment proof is null, hide the row
		if (signature && paymentProof) {
			$(this).hide();
		} else {
			$(this).show();
		}
	});
});

// Event listener for Ready to proceed button
$('.ready_to_proceed').on('click', function() {
	// Loop through each table row
	$('#ongoing-table tbody tr').each(function() {
		var _status = $(this).find('td:nth-child(5)').text();
		var signature = $(this).find('td:nth-child(11)').text();
		var paymentProof = $(this).find('td:nth-child(12)').text();
		console.log(_status);
		if ((!signature || !paymentProof) || (_status === 'closed')) {
			$(this).hide();
		} else {
			$(this).show();
		}
	});
});

