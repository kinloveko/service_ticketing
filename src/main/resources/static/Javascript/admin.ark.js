/**
 * for image viewer
 */

//to prevent user to go back after a logout

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
	$('.delete_own_account_btn').on('click', function(event) {

		var userID = $('#user_id_admin_id').val();
		console.log(userID);
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

				xhr.open('DELETE', '/user/delete/' + userID);

				xhr.onreadystatechange = function() {
					if (this.readyState === XMLHttpRequest.DONE) {
						if (this.status === 200) {
							// Show a success message
							Swal.fire({
								icon: 'success',
								title: 'Successfully Deleted',
								text: 'Your account has been successfully deleted. We\'re sorry to see you go.!'
							}).then((result) => {
								if (result.isConfirmed) {
							  window.location.href = "/";
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
	});
});





$(document).ready(function() {
	var user_id__ = $('#user_id_admin_id').val();
	$('.change_password_btn').on('click', function(event) {

		event.preventDefault();
		var user_password_ = $('#user_password_admin').val();


		$('#user_id__').val(user_id__);
		$('#pass_').val(user_password_);
		$('#change_password_modal').modal();
	});

	$('.change_pass_verify').on('click', function(event) {
	
		event.preventDefault();

		var password_ = $('#pass_').val();
		var verify_pass = $('#password_user').val();

		if (password_ === verify_pass) {
			Swal.fire({
				title: 'Password match!',
				text: 'Click okay to continue!',
				icon: 'success',
				confirmButtonText: 'OK',
				allowOutsideClick: false
			}).then((s) => {
				if (s.isConfirmed) {
					$('#change_password_modal').modal('hide');
					$('#change_password_modals').modal();

				}
			});
		} else {
			Swal.fire({
				icon: 'error',
				title: 'Wrong password!',
				text: 'Please try again! '
			});
		}
	});

	$('.confirm_new_password').on('click', function(event) {

		event.preventDefault();
		var newpassword = $('#newpassword').val();
		var confirmPassword = $('#confirmPassword').val();


		if (newpassword === '' || newpassword === null && confirmPassword === '' || confirmPassword === null) {
			Swal.fire({
				icon: 'error',
				title: 'Field is empty',
				text: 'Please input a new password! '
			});
		}
		else {
			
			const userID = user_id__;
			console.log(userID);
			const formData = new FormData();
			const newpassword = $('#newpassword').val();
			console.log(newpassword);
			formData.append('user_password', newpassword);
			if (newpassword === confirmPassword) {
				Swal.fire({
					title: 'Password Change Successfully!',
					text: 'Click okay to continue!',
					icon: 'success',
					confirmButtonText: 'OK',
					allowOutsideClick: false
				}).then((s) => {
					if (s.isConfirmed) {

						const xhr = new XMLHttpRequest();
						xhr.open('PUT', '/user/update-password/' + userID);
						xhr.onreadystatechange = function() {
							if (this.readyState === XMLHttpRequest.DONE) {
								if (this.status === 200) {
									// Show a success message
									Swal.fire({
										icon: 'success',
										title: 'Success',
										text: 'Updated Successfully!'
									}).then((result) => {
										if (result.isConfirmed) {
											// Reload the data
											$('#change_password_modals').modal('hide');
											$('#change_password_modal').modal('hide');
											$('#user_password_').val(formData.get('user_password'));


											$('#password_user').val('');
											$('#newpassword').val('');
											$('#confirmPassword').val('');

										}
									});
								}
							}
						};
						xhr.send(formData);

					}
				});
			}
			else {
				Swal.fire({
					icon: 'error',
					title: 'Doesn\'t matched!',
					text: 'Confirm password Doesn\'t matched 3! '
				});
			}

		}

	});

});




$(document).ready(function() {
	// trigger file input click when user clicks on image
	$('#user_image').on('click', function(event) {
		event.preventDefault();
		$('#profile_image_input').click();
	});

	$('#profile_image_input').on('change', function() {
		const file = this.files[0];
		if (file) {
			$('#user_image').attr('src', URL.createObjectURL(file));
			$('#image-profile').attr('src', URL.createObjectURL(file));
		}
	});


	$('#profile-table').on('click', '.save_changes_btn', function(event) {
		event.preventDefault();

		const form = document.getElementById("form-profile-data");
		const formData = new FormData(form);

		// Show loading animation with Swal
		Swal.fire({
			title: 'Update profile info?',
			text: 'Click confirm if you want to continue!',
			icon: 'info',
			showCancelButton: true,
			confirmButtonColor: '#5cb85c',
			cancelButtonColor: '#3085d6',
			confirmButtonText: 'Confirm',
			cancelButtonText: 'Cancel'
		}).then((result) => {
			if (result.isConfirmed) {
				const xhr = new XMLHttpRequest();
				xhr.open('PUT', '/user/update/profile');
				xhr.onreadystatechange = function() {
					if (this.readyState === XMLHttpRequest.DONE) {
						if (this.status === 200) {
							// Show a success message
							Swal.fire({
								icon: 'success',
								title: 'Success',
								text: 'Updated Successfully!'
							}).then((result) => {
								if (result.isConfirmed) {
									// Reload the data
									$('#user_name_admin').val(formData.get('user_name'));
									$('#user_email_admin').val(formData.get('user_email'));
									$('#address_admin').val(formData.get('address'));
									$('#contactNumber_admin').val(formData.get('contactNumber'));
									$('#user_status_admin').val(formData.get('status'));
									$('#userRole_user_admin').val(formData.get('userRole'));
									$('#user_password_admin').val(formData.get('user_password'));
									$('#user_id_admin_id').val(formData.get('user_id'));

									$('#profile_image_input').on('change', function() {
										const file = this.files[0];
										if (file) {
											$('#user_image').attr('src', URL.createObjectURL(file));
											$('#image-profile').attr('src', URL.createObjectURL(file));
										}
									});

								}
							});
						} else {
							// Show an error message
							Swal.fire({
								icon: 'error',
								title: 'Error',
								text: 'There was an error while updating your data: ' + this.statusText
							});
						}
					}
				};
				xhr.send(formData);
			}
		});
	});
});




$(document).ready(function() {



	// Attach click event handler to the parent of the delete buttons using event delegation
	$('#account-table').on('click', '.delete_user_btn', function(event) {
		event.preventDefault();
		deleteUserTicket($(this));
	});

	function deleteUserTicket(button) {
		var tableId = "account-table";
		var userID = button.closest('tr').find('td:eq(0)').text();
		console.log(userID);
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

				xhr.open('DELETE', '/user/delete/' + userID);

				xhr.onreadystatechange = function() {
					if (this.readyState === XMLHttpRequest.DONE) {
						if (this.status === 200) {
							// Show a success message
							Swal.fire({
								icon: 'success',
								title: 'Success',
								text: 'User Deleted successfully!'
							}).then((result) => {
								if (result.isConfirmed) {
									// Reload the data
									$('#' + tableId).load('/getUserUpdate?tableId=' + tableId + ' #' + tableId + ' > *', function() {
										// callback function

										$('.account_modal').on('click', function(event) {
											// Extract data from the input fields in the second modal
											var _userid = $(this).closest('tr').find('td:eq(0)').text();
											var _username = $(this).closest('tr').find('td:eq(1)').text();

											var _useremail = $(this).closest('tr').find('td:eq(2)').text();
											var _role = $(this).closest('tr').find('td:eq(3)').text();
											var _status = $(this).closest('tr').find('td:eq(4)').text();
											var password = $(this).closest('tr').find('td:eq(5)').text();
											$('#user_id_').val(_userid);
											$('#user_name_').val(_username);
											$('#user_email_').val(_useremail);
											$('#role').val(_role);
											$('#user_status').val(_status);
											$('#user_password_').val(password);
											$('#accountModal').modal();

										});


									});

								}
							});
						} else {
							// Show an error message
							Swal.fire({
								icon: 'error',
								title: 'Error',
								text: 'There was an error while deleting the user: ' + this.statusText
							});
						}
					}
				};

				xhr.send();
			}
		});
	}
});







$(document).ready(function() {

	// Attach click event handler to the parent of the delete buttons using event delegation
	$('#ongoing-table').on('click', '.delete_ongoing_ticket', function(event) {
		event.preventDefault();
		deleteUserTicket($(this));
	});

	function deleteUserTicket(button) {
		var tableId = "ongoing-table";
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
									$('#' + tableId).load('/getUpdateTicket?tableId=' + tableId + ' #' + tableId + ' > *', function() {
										// callback function
										// callback function to update the value of the pending count span
										var ongoingCount = parseInt($('#ongoing_count').text());
										$('#ongoing_count').text(ongoingCount - 1);
									});

									//ONGOING MODAL
									$(document).ready(function() {
										$('.eBtn').on('click', function(event) {
											var ticketId = $(this).closest('tr').find('td:eq(0)').text();
											var dateCreated = $(this).closest('tr').find('td:eq(1)').text();
											var title = $(this).closest('tr').find('td:eq(2)').text();
											var description = $(this).closest('tr').find('td:eq(3)').text();
											var userName = $(this).closest('tr').find('td:eq(4)').text();
											var status = $(this).closest('tr').find('td:eq(5)').text();
											var conformeNo = $(this).closest('tr').find('td:eq(6)').text();
											$('#id').val(ticketId);
											$('#date').val(dateCreated);
											$('#title').val(title);
											$('#description').val(description);
											$('#name').val(userName);
											$('#status').val(status);
											$('#conforme_no').val(conformeNo);
											$('#modalView').modal();
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

$(document).ready(function() {
	$('.send_to_client').on('click', function() {
		// Show the modal
		var ticket = $('#ticket_id_view').val();
		var email = $('#client_name_invoice_view').val();
		var name = $('#client_email_invoice_view').val();


		$('#ticket_id_email').val(ticket);
		$('#email_client_name').val(email);
		$('#email_client').val(name);
		$('#view_invoice_modals').modal('hide');
		$('#email_client_modal').modal();

	});

	$('.send_to_client_btn').on('click', function() {



		var ticketID = $('#ticket_id_view').val();
		var form = document.getElementById("email-client-form");
		var formData = new FormData(form);
		var xhr = new XMLHttpRequest();
		var url = "/send/confirmation/client/" + ticketID;
		var tableId = "completed-table";
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
				console.log("User updated successfully");

				Swal.fire({
					icon: 'success',
					title: 'Success',
					text: 'Email sent updated successfully!',
				}).then((result) => {
					if (result.isConfirmed) {

						$('#email_client_modal').modal('hide');
						$('#' + tableId).load('/getUpdateTicket?tableId=' + tableId + ' #' + tableId + ' > *', function() {
							// callback function
							// Event listener for On-going Tickets button
							$('.menu-item[data-table="completed"]').on('click', function() {
								// Show all rows
								$('#completed-table tbody tr').show();

								// Remove filters from other buttons
								$('.sales_team_button').removeClass('active');
								$('.billing_team_button').removeClass('active');
								$('.collection_team_button').removeClass('active');
								$('.treasury_team_button').removeClass('active');
							});

							// Event listener for Ready sales_team button
							$('.sales_team_button').on('click', function() {
								// Loop through each table row
								$('#completed-table tbody tr').each(function() {
									var _status = $(this).find('td:nth-child(5)').text();
									var _progress = $(this).find('td:nth-child(8)').text();
									console.log(_status);

									if (!_status === 'completed' || !(_progress === 'support_team')) {
										$(this).hide();
									} else {
										$(this).show();
									}
								});
							});

							// Event listener for Ready billing team button
							$('.billing_team_button').on('click', function() {
								// Loop through each table row
								applyBillingFilter();
							});

							function applyBillingFilter() {
								// Loop through each table row
								$('#completed-table tbody tr').each(function() {
									var _status = $(this).find('td:nth-child(5)').text();
									var _progress = $(this).find('td:nth-child(8)').text();
									console.log(_status);
									console.log(_progress);
									if (!(_status === 'completed') || !(_progress === 'billing_team')) {
										$(this).hide();
									} else {
										$(this).show();
									}
								});
							}



							$('.collection_team_button').on('click', function() {
								// Loop through each table row
								$('#completed-table tbody tr').each(function() {
									var _status = $(this).find('td:nth-child(5)').text();
									var _progress = $(this).find('td:nth-child(8)').text();
									console.log(_status);
									if (!(_status === 'completed') || !(_progress === 'collection_team')) {
										$(this).hide();
									} else {
										$(this).show();
									}
								});
							});

							// Event listener for Ready treasury team button
							$('.treasury_team_button').on('click', function() {
								// Loop through each table row
								$('#completed-table tbody tr').each(function() {
									var _status = $(this).find('td:nth-child(5)').text();
									var _progress = $(this).find('td:nth-child(8)').text();
									console.log(_status);
									if (!(_status === 'completed') || !(_progress === 'treasury_team')) {
										$(this).hide();
									} else {
										$(this).show();
									}
								});
							});
							$('.completed_team').on('click', function() {
								// Loop through each table row
								$('#completed-table tbody tr').each(function() {
									var _status = $(this).find('td:nth-child(5)').text();
									var _progress = $(this).find('td:nth-child(8)').text();
									console.log(_status);
									if (!(_status === 'completed') || !(_progress === 'completed')) {
										$(this).hide();
									} else {
										$(this).show();
									}
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
	$('.aging_tickets').on('click', function() {
		// Show the modal
		$('#aging_ticket_modal').modal();
	});

	$('.download_excel_aging').on('click', function() {
		// Get the select element value
		const dropdownValue = $('#dropdownAging').val();

		// Show error if the selected value is "default"
		if (dropdownValue === "default") {
			Swal.fire({
				title: 'Error',
				text: 'Please choose a filter',
				icon: 'error',
				confirmButtonText: 'OK'
			});
			return false;
		} else {
			// Show success if the selected value is not "default"
			Swal.fire({
				title: 'Success',
				text: 'Excel file download successfully!',
				icon: 'success',
				confirmButtonText: 'OK'
			}).then((result) => {
				if (result.isConfirmed) {
					// Close the modal
					console.log(dropdownValue);
					$('#aging_ticket_modal').modal('hide');
				}
			});
			return true;
		}
	});
});


$(document).ready(function() {
	$('.ticket_per_assignee').on('click', function() {
		// Show the modal
		$('#ticket_per_assignee_modal').modal();
	});

	$('.download_excel_asignee').on('click', function() {
		// Get the select element value
		const dropdownValue = $('#dropdownRoleAssignee').val();

		// Show error if the selected value is "default"
		if (dropdownValue === "default") {
			Swal.fire({
				title: 'Error',
				text: 'Please choose a filter',
				icon: 'error',
				confirmButtonText: 'OK'
			});
			return false;
		} else {
			// Show success if the selected value is not "default"
			Swal.fire({
				title: 'Success',
				text: 'Excel file download successfully!',
				icon: 'success',
				confirmButtonText: 'OK'
			}).then((result) => {
				if (result.isConfirmed) {
					// Close the modal
					$('#ticket_per_assignee_modal').modal('hide');
				}
			});
			return true;
		}
	});
});


$(document).ready(function() {
	$('.monthly_report').on('click', function() {
		// Show the modal
		$('#monthly_report_filter_modal').modal();
	});

	$('.download_excel').on('click', function() {
		// Get the select element value
		const dropdownValue = $('#dropdownRole').val();

		// Show error if the selected value is "default"
		if (dropdownValue === "default") {
			Swal.fire({
				title: 'Error',
				text: 'Please choose a filter',
				icon: 'error',
				confirmButtonText: 'OK'
			});
			return false;
		} else {
			// Show success if the selected value is not "default"
			Swal.fire({
				title: 'Success',
				text: 'Conforme slip generated successfully!',
				icon: 'success',
				confirmButtonText: 'OK'
			}).then((result) => {
				if (result.isConfirmed) {
					// Close the modal
					$('#monthly_report_filter_modal').modal('hide');
				}
			});
			return true;
		}
	});
});





$(document).ready(function() {

	$('.imageProof').on('click', function(event) {
		var image_proof = $("#client_payment_proof").attr("src");
		$('#imageViewerImage').attr('src', image_proof);
		$('#imageViewerModal').modal();

	});

});



$(document).ready(function() {

	$('.imageSignature').on('click', function(event) {
		var image_proof = $("#client_signature").attr("src");
		$('#imageViewerImage').attr('src', image_proof);
		$('#imageViewerModal').modal();
	});

});

$(document).ready(function() {
	$('#ticket_status').on('click', function(event) {
		var ticketId = $('#ticket_id').val(); // replace with the ticket ID that you want to use
		var url = '/tickets/' + ticketId; // replace with the URL pattern that you want to use
		window.location.href = url; // redirect to the new URL
	});
});


/*	Generating OR PDF*/

//purpose to prevent to refresh the page while inserting a new record
$(document).ready(function() {
	$('.generate_or').on('click', function() {
		// Extract data from the input fields in the second modal

		var ticketID = $('#ticket_id_view').val();
		console.log(ticketID);
		var xhr = new XMLHttpRequest();
		var url = "/pdf/generate/" + ticketID;

		// Show loading animation with Swal
		Swal.fire({
			title: 'Loading...',
			allowEscapeKey: false,
			allowOutsideClick: false,
			didOpen: () => {
				Swal.showLoading();
			}
		});

		xhr.open("GET", url);
		xhr.onload = function() {
			if (xhr.status === 200) {
				// Show a success message
				console.log("PDF");

				Swal.fire({
					icon: 'success',
					title: 'Success',
					text: 'OR Generated successfully!',
				}).then((result) => {
					if (result.isConfirmed) {

						window.open('/pdf/generate/' + ticketID);


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
		xhr.send();
	});
});

//ongoing button


$(document).ready(function() {
	$('.proceed_to_support_btn').on('click', function() {
		// Extract data from the input fields in the second modal
		/*  var imageUpload = $('#image_upload').prop('files')[0];*/
		event.preventDefault();
		var ticketId = $('#ticket_id').val();
		var formData = new FormData();
		formData.append('progress', 'support_team');
		formData.append('status', 'ongoing');


		var tableIdOngoing = "ongoing-table";
		// Show loading animation with Swal
		Swal.fire({
			title: 'Loading...',
			allowEscapeKey: false,
			allowOutsideClick: false,
			didOpen: () => {
				Swal.showLoading();
			}
		});

		var xhr = new XMLHttpRequest();
		xhr.open('PUT', '/tickets/update-ticket/' + ticketId);
		xhr.onload = function() {
			if (xhr.status === 200) {
				// Show a success message
				console.log("User updated successfully");

				Swal.fire({
					icon: 'success',
					title: 'Success',
					text: 'Conform slip generated successfully!',
				}).then((result) => {
					if (result.isConfirmed) {
						$('#ongoingModal').modal('hide');
						$('#proceed_support').hide();

						$('#' + tableIdOngoing).load('/getUpdateTicket?tableId=' + tableIdOngoing + ' #' + tableIdOngoing + ' > *', function() {
							// callback function
							//Just to refresh the buttons functionality from ongoing table
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

						});

					} else {
						// Show error message with SweetAlert
						Swal.fire({
							icon: 'error',
							title: 'Error',
							text: 'There was an error while saving the ticket: ' + xhr.statusText,
						});
					}
				});

			} else {
				// Show an error message
				console.log("Error Updating Ticket");
			}
		};
		xhr.send(formData);
	});
});


//pending button

$(document).ready(function() {
	$('.conforme-button').on('click', function() {
		// Extract data from the input fields in the second modal
		/*  var imageUpload = $('#image_upload').prop('files')[0];*/
		event.preventDefault();

		var pendingform = document.getElementById("pending-form");
		var pendingData = new FormData(pendingform);
		var xhr = new XMLHttpRequest();
		var url = "/tickets/update_ticket";
		var tableId = "pending-table";
		var tableIdOngoing = "ongoing-table";
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
				console.log("User updated successfully");

				Swal.fire({
					icon: 'success',
					title: 'Success',
					text: 'Conform slip generated successfully!',
				}).then((result) => {
					if (result.isConfirmed) {

						$('#create_conforme').modal('hide');
						$('#viewTicket').modal('hide');

						$('#' + tableId).load('/getUpdateTicket?tableId=' + tableId + ' #' + tableId + ' > *', function() {
							// callback function
							// callback function to update the value of the pending count span
							var pendingCount = parseInt($('#pending_count').text());
							$('#pending_count').text(pendingCount - 1);

						});

						$('#' + tableIdOngoing).load('/getUpdateTicket?tableId=' + tableIdOngoing + ' #' + tableIdOngoing + ' > *', function() {
							// callback function
							//Just to refresh the buttons functionality from ongoing table
							/*MENU BUTTONS FOR ONGOING TABLE*/

							//Button for ongoing 
							$(document).ready(function() {

								$('.ongoingBtn').on('click', function() {

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
									var userID = $(this).closest('tr').find('td:eq(13)').text();

									var toBilling = "billing_team";
									var toSupport = "support_team";

									console.log(status);
									console.log(progress);
									$('#thumbnail').attr('src', '/img/' + signature);


									if (progress === "support_team" || progress === "billing_team" || progress === "collection_team" || progress === "treasury_team" || progress === "completed") {

										if (status === "completed") {

											if (progress === "billing_team") {
												$('#proceed_billing').hide();
												$('#proceed_collection').show();
												$('#proceed_treasury').hide();
												$('#invoice_modal_details').hide();
												$('#send_to_collection').hide();
												$('#proof_confirmation_treasury').hide();
												$('#generateOR').hide();
											}
											else
												if (progress === "collection_team") {
													$('#send_to_collection').hide();
													$('#proceed_collection').hide();
													$('#proceed_treasury').show();
													$('#proceed_billing').hide();
													$('#proof_confirmation_treasury').hide();
													$('#invoice_modal_details').show();
													$('#generateOR').hide();
												}
												else
													if (progress === "treasury_team") {
														$('#send_to_collection').show();
														$('#proceed_collection').hide();
														$('#invoice_modal_details').show();
														$('#proof_confirmation_treasury').show();
														$('#proceed_billing').hide();
														$('#proceed_treasury').hide();
														$('#generateOR').hide();
													} else
														if (progress === "completed") {
															$('#generateOR').show();
															$('#send_to_collection').hide();
															$('#proceed_collection').hide();
															$('#invoice_modal_details').show();
															$('#proceed_billing').hide();
															$('#proceed_treasury').hide();
															$('#proceed_collection').hide();
															$('#proof_confirmation_treasury').hide();

														}
														else {
															$('#generateOR').hide();
															$('#proof_confirmation_treasury').hide();
															$('#invoice_modal_details').hide();
															$('#proceed_collection').hide();
															$('#progress_ongoing').val(toBilling);
															$('#proceed_billing').show();
															$('#span_warning').hide();
															$('#proceed_treasury').hide();
														}

										}
										else {
											$('#generateOR').hide();
											$('#invoice_modal_details').hide();
											$('#proceed_treasury').hide();
											$('#proceed_collection').hide();
											$('#proceed_billing').hide();
											$('#span_warning').show();

										}

										$('#ticket_status').show();
										$('#proceed_support').hide();
										// Add the justify-content-between class
										$('#buttonDiv').addClass('justify-content-between');
									}
									else {
										console.log(progress);
										$('#span_warning').hide();
										$('#progress_ongoing').val(toSupport);
										$('#proceed_billing').hide();

										$('#ticket_status').hide();
										// Remove the justify-content-between class
										$('#buttonDiv').removeClass('justify-content-between');

									}

									if (client_signature && client_payment_proof) {
										//images 

										//so that when the admin will click the button it will rewrite the data 

										$('#waiting_payment').hide();
										$('#waiting_signature').hide();
										$('#client_payment_proof').show();
										$('#client_signature').show();

										$('#span_warning_support').hide();


										$('#client_payment_proof').attr('src', '/img/' + client_payment_proof);
										$('#client_signature').attr('src', '/img/' + client_signature);

										$('#client_payment_proofs').val(client_payment_proof);
										$('#client_signatures').val(client_signature);
										$('#salesSignatures').val(signature);


										//this is just to check if progress is not going to billing or to support team
										$('#proceed_support').attr({
											"disabled": false
										});

									} else {

										$('#waiting_payment').show();
										$('#waiting_signature').show();
										$('#client_payment_proof').hide();
										$('#client_signature').hide();
										$('#span_warning_support').show();
										$('#proceed_support').attr({
											"disabled": true
										});
									}


									$('#user_id').val(userID);
									$('#ticket_id').val(ticketId);
									$('#conforme_date_ongoing').val(conforme_date_ongoing);
									$('#title_ongoing').val(title);
									$('#description_ongoing').val(description);
									$('#user_name_ongoing').val(userName);
									$('#status__').val(status);
									$('#amount_ongoing').val(amount);
									$('#conforme_no_ongoing').val(conformeNo); // set the conforme_no value in the #modalView modal
									$('#ongoingModal').modal();
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
		xhr.send(pendingData);

	});
});





/*Sending to collection team button*/


$(document).ready(function() {
	$('.send_to_collection_button').on('click', function() {
		// Extract data from the input fields in the second modal
		/*  var imageUpload = $('#image_upload').prop('files')[0];*/
		var ticketID = $('#ticket_id_view').val();
		var form = document.getElementById("invoice-update-confirm");
		var formData = new FormData(form);
		var xhr = new XMLHttpRequest();
		var url = "/invoice/update/" + ticketID;
		var tableId = "completed-table";
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
				console.log("User updated successfully");

				Swal.fire({
					icon: 'success',
					title: 'Success',
					text: 'Ticket updated successfully!',
				}).then((result) => {
					if (result.isConfirmed) {
						$('#view_invoice_modals').modal('hide');
						$('#send_to_collection').hide();
						$('#proof_confirmation_treasury').hide();

						$('#' + tableId).load('/getUpdateTicket?tableId=' + tableId + ' #' + tableId + ' > *', function() {
							// callback function



							$('.menu-item[data-table="completed"]').on('click', function() {
								// Show all rows
								$('#completed-table tbody tr').show();


								// Remove filters from other buttons
								$('.sales_team_button').removeClass('active');
								$('.billing_team_button').removeClass('active');
								$('.collection_team_button').removeClass('active');
								$('.treasury_team_button').removeClass('active');
							});

							// Event listener for Ready sales_team button
							$('.sales_team_button').on('click', function() {
								// Loop through each table row
								$('#completed-table tbody tr').each(function() {
									var _status = $(this).find('td:nth-child(5)').text();
									var _progress = $(this).find('td:nth-child(8)').text();
									console.log(_status);

									if (!_status === 'completed' || !(_progress === 'support_team')) {
										$(this).hide();
									} else {
										$(this).show();
									}
								});
							});

							// Event listener for Ready billing team button
							$('.billing_team_button').on('click', function() {
								// Loop through each table row
								applyBillingFilter();
							});

							function applyBillingFilter() {
								// Loop through each table row
								$('#completed-table tbody tr').each(function() {
									var _status = $(this).find('td:nth-child(5)').text();
									var _progress = $(this).find('td:nth-child(8)').text();
									console.log(_status);
									console.log(_progress);
									if (!(_status === 'completed') || !(_progress === 'billing_team')) {
										$(this).hide();
									} else {
										$(this).show();
									}
								});
							}


							$('.collection_team_button').on('click', function() {
								// Loop through each table row
								$('#completed-table tbody tr').each(function() {
									var _status = $(this).find('td:nth-child(5)').text();
									var _progress = $(this).find('td:nth-child(8)').text();
									console.log(_status);
									if (!(_status === 'completed') || !(_progress === 'collection_team')) {
										$(this).hide();
									} else {
										$(this).show();
									}
								});
							});

							// Event listener for Ready treasury team button
							$('.treasury_team_button').on('click', function() {
								// Loop through each table row
								$('#completed-table tbody tr').each(function() {
									var _status = $(this).find('td:nth-child(5)').text();
									var _progress = $(this).find('td:nth-child(8)').text();
									console.log(_status);
									if (!(_status === 'completed') || !(_progress === 'treasury_team')) {
										$(this).hide();
									} else {
										$(this).show();
									}
								});
							});
							$('.completed_team').on('click', function() {
								// Loop through each table row
								$('#completed-table tbody tr').each(function() {
									var _status = $(this).find('td:nth-child(5)').text();
									var _progress = $(this).find('td:nth-child(8)').text();
									console.log(_status);
									if (!(_status === 'completed') || !(_progress === 'completed')) {
										$(this).hide();
									} else {
										$(this).show();
									}
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


/*TREASURY BUTTON*/


$('.procceed_treasury').on('click', function(event) {

	event.preventDefault();

	var currentDate = new Date().toLocaleString('en-US');
	var clientSignature = $('#client_signatures').val();
	var clientPaymentProof = $('#client_payment_proofs').val();
	var ticketId = $('#ticket_id').val();
	var titleInvoice = $('#title_ongoing').val();
	var clientName = $('#user_name_ongoing').val();
	var amount_invoice = $('#amount_ongoing').val();
	var userId = $('#user_id').val();
	var tableId = "completed-table";

	$('#invoice_date_now').val(currentDate);
	$('#ticket_id_invoice').val(ticketId);
	$('#ticket_title_invoice').val(titleInvoice);

	$('#client_name_invoice').val(clientName);
	$('#amount_invoice').val(amount_invoice);

	//for image display
	$('#client_signature_invoice').attr('src', '/img/' + clientSignature);
	$('#client_payment_proof_invoice').attr('src', '/img/' + clientPaymentProof);
	//for inputs hiiden to be saved
	$('#client_signatures_hidden').val(clientSignature);
	$('#client_payment_proofs_hidden').val(clientPaymentProof);


	updateTicket();
	function updateTicket() {
		var xhr = new XMLHttpRequest();
		xhr.open('PUT', '/tickets/update-ticket/' + ticketId);
		xhr.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');
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

						$('#ongoingModal').modal('hide');
						$('#proceed_treasury').hide();
						// Update the content using JavaScript here
						// For example, if you have a div with id "completed-table" that displays the completed tickets, you can update it like this:
						$('#' + tableId).load('/getUpdateTicket?tableId=' + tableId + ' #' + tableId + ' > *', function() {
							// callback function
							// Event listener for On-going Tickets button

							$('.menu-item[data-table="completed"]').on('click', function() {
								// Show all rows
								$('#completed-table tbody tr').show();


								// Remove filters from other buttons
								$('.sales_team_button').removeClass('active');
								$('.billing_team_button').removeClass('active');
								$('.collection_team_button').removeClass('active');
								$('.treasury_team_button').removeClass('active');
							});

							// Event listener for Ready sales_team button
							$('.sales_team_button').on('click', function() {
								// Loop through each table row
								$('#completed-table tbody tr').each(function() {
									var _status = $(this).find('td:nth-child(5)').text();
									var _progress = $(this).find('td:nth-child(8)').text();
									console.log(_status);

									if (!_status === 'completed' || !(_progress === 'support_team')) {
										$(this).hide();
									} else {
										$(this).show();
									}
								});
							});

							// Event listener for Ready billing team button
							$('.billing_team_button').on('click', function() {
								// Loop through each table row
								applyBillingFilter();
							});

							function applyBillingFilter() {
								// Loop through each table row
								$('#completed-table tbody tr').each(function() {
									var _status = $(this).find('td:nth-child(5)').text();
									var _progress = $(this).find('td:nth-child(8)').text();
									console.log(_status);
									console.log(_progress);
									if (!(_status === 'completed') || !(_progress === 'billing_team')) {
										$(this).hide();
									} else {
										$(this).show();
									}
								});
							}


							$('.collection_team_button').on('click', function() {
								// Loop through each table row
								$('#completed-table tbody tr').each(function() {
									var _status = $(this).find('td:nth-child(5)').text();
									var _progress = $(this).find('td:nth-child(8)').text();
									console.log(_status);
									if (!(_status === 'completed') || !(_progress === 'collection_team')) {
										$(this).hide();
									} else {
										$(this).show();
									}
								});
							});

							// Event listener for Ready treasury team button
							$('.treasury_team_button').on('click', function() {
								// Loop through each table row
								$('#completed-table tbody tr').each(function() {
									var _status = $(this).find('td:nth-child(5)').text();
									var _progress = $(this).find('td:nth-child(8)').text();
									console.log(_status);
									if (!(_status === 'completed') || !(_progress === 'treasury_team')) {
										$(this).hide();
									} else {
										$(this).show();
									}
								});
							});
							$('.completed_team').on('click', function() {
								// Loop through each table row
								$('#completed-table tbody tr').each(function() {
									var _status = $(this).find('td:nth-child(5)').text();
									var _progress = $(this).find('td:nth-child(8)').text();
									console.log(_status);
									if (!(_status === 'completed') || !(_progress === 'completed')) {
										$(this).hide();
									} else {
										$(this).show();
									}
								});
							});


						});
					} else {
						// Show error message with SweetAlert
						Swal.fire({
							icon: 'error',
							title: 'Error',
							text: 'There was an error while saving the invoice: ' + xhr.statusText,
						});
					}
				});

			} else {
				// Show an error message
				console.log("Error updating ticket");
			}
		};
		xhr.send("progress=treasury_team&status=completed");
	}

});

/*CREATING INVOICE BUTTON*/


$('.create_invoice').on('click', function(event) {

	event.preventDefault();

	var currentDate = new Date().toLocaleString('en-US');
	var clientSignature = $('#client_signatures').val();
	var clientPaymentProof = $('#client_payment_proofs').val();
	var ticketId = $('#ticket_id').val();
	var titleInvoice = $('#title_ongoing').val();
	var clientName = $('#user_name_ongoing').val();
	var amount_invoice = $('#amount_ongoing').val();
	var userId = $('#user_id').val();
	var tableId = "completed-table";
	console.log(userId);
	$('#invoice_date_now').val(currentDate);
	$('#ticket_id_invoice').val(ticketId);
	$('#ticket_title_invoice').val(titleInvoice);

	$('#client_name_invoice').val(clientName);
	$('#amount_invoice').val(amount_invoice);

	//for image display
	$('#client_signature_invoice').attr('src', '/img/' + clientSignature);
	$('#client_payment_proof_invoice').attr('src', '/img/' + clientPaymentProof);
	//for inputs hiiden to be saved
	$('#client_signatures_hidden').val(clientSignature);
	$('#client_payment_proofs_hidden').val(clientPaymentProof);

	var formData = $('#invoice-form').serialize();

	$.ajax({
		type: "PUT",
		url: "/tickets/update-ticket/" + ticketId,
		data: {
			"progress": "collection_team",
			"status": "completed"
		},
		success: function() {
			// Show a success message
			console.log("Ticket updated successfully");



			// Call the function to update another repository
			updateRepository();
		}
	});

	function updateRepository() {
		var xhr = new XMLHttpRequest();
		xhr.open('POST', '/invoice/save');
		xhr.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');
		xhr.onload = function() {
			if (xhr.status === 200) {
				// Show success message with SweetAlert

				Swal.fire({
					icon: 'success',
					title: 'Success',
					text: 'The invoice has been created successfully!',
				}).then((result) => {
					if (result.isConfirmed) {
						$('#invoice_modal').modal('hide');
						$('#proceed_collection').hide();
						$('#invoice_modal_details').show();
						// Update the content using JavaScript here
						// For example, if you have a div with id "completed-table" that displays the completed tickets, you can update it like this:
						$('#' + tableId).load('/getUpdateTicket?tableId=' + tableId + ' #' + tableId + ' > *', function() {
							// Event listener for On-going Tickets button
							$('.menu-item[data-table="completed"]').on('click', function() {
								// Show all rows
								$('#completed-table tbody tr').show();

								// Remove filters from other buttons
								$('.sales_team_button').removeClass('active');
								$('.billing_team_button').removeClass('active');
								$('.collection_team_button').removeClass('active');
								$('.treasury_team_button').removeClass('active');
							});

							// Event listener for Ready sales_team button
							$('.sales_team_button').on('click', function() {
								// Loop through each table row
								$('#completed-table tbody tr').each(function() {
									var _status = $(this).find('td:nth-child(5)').text();
									var _progress = $(this).find('td:nth-child(8)').text();
									console.log(_status);

									if (!_status === 'completed' || !(_progress === 'support_team')) {
										$(this).hide();
									} else {
										$(this).show();
									}
								});
							});

							// Event listener for Ready billing team button
							$('.billing_team_button').on('click', function() {
								// Loop through each table row
								applyBillingFilter();
							});

							function applyBillingFilter() {
								// Loop through each table row
								$('#completed-table tbody tr').each(function() {
									var _status = $(this).find('td:nth-child(5)').text();
									var _progress = $(this).find('td:nth-child(8)').text();
									console.log(_status);
									console.log(_progress);
									if (!(_status === 'completed') || !(_progress === 'billing_team')) {
										$(this).hide();
									} else {
										$(this).show();
									}
								});
							}



							$('.collection_team_button').on('click', function() {
								// Loop through each table row
								$('#completed-table tbody tr').each(function() {
									var _status = $(this).find('td:nth-child(5)').text();
									var _progress = $(this).find('td:nth-child(8)').text();
									console.log(_status);
									if (!(_status === 'completed') || !(_progress === 'collection_team')) {
										$(this).hide();
									} else {
										$(this).show();
									}
								});
							});

							// Event listener for Ready treasury team button
							$('.treasury_team_button').on('click', function() {
								// Loop through each table row
								$('#completed-table tbody tr').each(function() {
									var _status = $(this).find('td:nth-child(5)').text();
									var _progress = $(this).find('td:nth-child(8)').text();
									console.log(_status);
									if (!(_status === 'completed') || !(_progress === 'treasury_team')) {
										$(this).hide();
									} else {
										$(this).show();
									}
								});
							});
							$('.completed_team').on('click', function() {
								// Loop through each table row
								$('#completed-table tbody tr').each(function() {
									var _status = $(this).find('td:nth-child(5)').text();
									var _progress = $(this).find('td:nth-child(8)').text();
									console.log(_status);
									if (!(_status === 'completed') || !(_progress === 'completed')) {
										$(this).hide();
									} else {
										$(this).show();
									}
								});
							});


						});
					}
				});
			} else {
				// Show error message with SweetAlert
				Swal.fire({
					icon: 'error',
					title: 'Error',
					text: 'There was an error while saving the invoice: ' + xhr.statusText,
				});
			}
		};
		xhr.onerror = function() {
			// Show error message with SweetAlert
			Swal.fire({
				icon: 'error',
				title: 'Error',
				text: 'There was an error while saving the invoice: ' + xhr.statusText,
			});
		};
		xhr.send(formData);
	}

	$('#invoice_modal').modal();
	$('#ongoingModal').modal('hide');
});


/*OPEN INVOICE MODAL BUTTON*/

//for opening the modal and setting the attributes to modal inputs and img
$('.create_invoce_modal').on('click', function(event) {
	var currentDate = new Date().toLocaleString('en-US');
	var clientSignature = $('#client_signatures').val();
	var clientPaymentProof = $('#client_payment_proofs').val();
	var ticketId = $('#ticket_id').val();
	var titleInvoice = $('#title_ongoing').val();
	var clientName = $('#user_name_ongoing').val();
	var amount_invoice = $('#amount_ongoing').val();
	var userId = $('#user_id').val();
	console.log(userId);
	$('#invoice_date_now').val(currentDate);
	$('#ticket_id_invoice').val(ticketId);
	$('#ticket_title_invoice').val(titleInvoice);

	$('#client_name_invoice').val(clientName);
	$('#amount_invoice').val(amount_invoice);

	//for image display
	$('#client_signature_invoice').attr('src', '/img/' + clientSignature);
	$('#client_payment_proof_invoice').attr('src', '/img/' + clientPaymentProof);
	//for inputs hiiden to be saved
	$('#client_signatures_hidden').val(clientSignature);
	$('#client_payment_proofs_hidden').val(clientPaymentProof);


	//Retrieved User using ajax before opening the invoice
	$.ajax({
		type: "GET",
		url: "/userDetails?userId=" + userId,
		success: function(email) {
			var user_email = $('#client_email_invoice');
			user_email.empty(); // clear existing content
			user_email.val(email);

		},
		error: function(jqXHR, textStatus, errorThrown) {
			// handle the error here
			console.log(textStatus);
		}
	});
	$('#invoice_modal').modal();
	$('#ongoingModal').modal('hide');
});


/*VIEW INVOICE MODAL BUTTON*/


$('.view_invoice_modal').on('click', function(event) {

	var ticketID = $('#ticket_id').val();

	//Retrieved User using ajax before opening the invoice



	$.ajax({
		type: "GET",
		url: "/getTicket?ticketID=" + ticketID,
		success: function(result) {
			var spanText = $('#ticket_progress_invoice');
			spanText.html('<strong>' + result + '</strong>');
			if (result === 'completed') {
				spanText.addClass('ticket-status');
			} else {
				spanText.text('');
			}

		},
		error: function(jqXHR, textStatus, errorThrown) {
			// handle the error here
			console.log(textStatus);
			console.log(errorThrown);
			console.log(jqXHR);

		}
	});





	$.ajax({
		type: "GET",
		url: "/invoiceDetails?ticketID=" + ticketID,
		success: function(invoiceDetails) {
			$('#invoice_date_now_view').val(invoiceDetails.invoice_date);
			$('#ticket_id_view').val(invoiceDetails.ticketID);
			$('#ticket_title_invoice_view').val(invoiceDetails.ticket_issue_title);

			/* 	$('#ticket_progress_invoice').text(invoiceDetails.progress); */


			$('#client_name_invoice_view').val(invoiceDetails.client_name);
			$('#amount_invoice_view').val(invoiceDetails.invoice_amount);
			$('#client_email_invoice_view').val(invoiceDetails.client_email);
			//for image display
			$('#client_signature_invoice_view').attr('src', '/img/' + invoiceDetails.client_signature);
			$('#client_payment_proof_invoice_view').attr('src', '/img/' + invoiceDetails.client_payment_proof);

			$('#client_signature_value').val(invoiceDetails.client_signature);
			$('#client_payment_proof_value').val(invoiceDetails.client_payment_proof);

			$('#view_invoice_modals').modal();
			$('#ongoingModal').modal('hide');

		},
		error: function(jqXHR, textStatus, errorThrown) {
			// handle the error here
			console.log(textStatus);
			console.log(errorThrown);
			console.log(jqXHR);
			swal({
				title: "Error",
				text: "Error occur! Query did not return a unique result: 2",
				icon: "error",
				button: "OK",
			});

		}
	});

});


/*SUPPORT BUTTON*/

$('.supportBtn').on('click', function(event) {
	// Extract data from the input fields in the second modal
	var ticketId = $(this).closest('tr').find('td:eq(0)').text();
	var title = $(this).closest('tr').find('td:eq(2)').text();
	var userName = $(this).closest('tr').find('td:eq(3)').text();
	var conformeNo = $(this).closest('tr').find('td:eq(6)').text();
	$('#ticket_id_support').val(ticketId);
	$('#conforme_no_support').val(conformeNo);
	$('#title_support').val(title);
	$('#user_name_support').val(userName);
	$('#support_modal').modal('show');

});



/*	ONGOING BUTTON*/

//Proceed Sales_Team to Billing 



$('.proceed_to_billing_btn').on('click', function(event) {

	event.preventDefault();


	var ticketId = $('#ticket_id').val();
	var tableId = "completed-table";


	updateTicket();
	function updateTicket() {
		var xhr = new XMLHttpRequest();
		xhr.open('PUT', '/tickets/update-ticket/' + ticketId);
		xhr.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');
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

						$('#ongoingModal').modal('hide');
						$('#proceed_billing').hide();
						// Update the content using JavaScript here
						// For example, if you have a div with id "completed-table" that displays the completed tickets, you can update it like this:
						$('#' + tableId).load('/getUpdateTicket?tableId=' + tableId + ' #' + tableId + ' > *', function() {
							// callback function
							// Event listener for On-going Tickets button

							$('.menu-item[data-table="completed"]').on('click', function() {
								// Show all rows
								$('#completed-table tbody tr').show();


								// Remove filters from other buttons
								$('.sales_team_button').removeClass('active');
								$('.billing_team_button').removeClass('active');
								$('.collection_team_button').removeClass('active');
								$('.treasury_team_button').removeClass('active');
							});

							// Event listener for Ready sales_team button
							$('.sales_team_button').on('click', function() {
								// Loop through each table row
								$('#completed-table tbody tr').each(function() {
									var _status = $(this).find('td:nth-child(5)').text();
									var _progress = $(this).find('td:nth-child(8)').text();
									console.log(_status);

									if (!_status === 'completed' || !(_progress === 'support_team')) {
										$(this).hide();
									} else {
										$(this).show();
									}
								});
							});

							// Event listener for Ready billing team button
							$('.billing_team_button').on('click', function() {
								// Loop through each table row
								applyBillingFilter();
							});

							function applyBillingFilter() {
								// Loop through each table row
								$('#completed-table tbody tr').each(function() {
									var _status = $(this).find('td:nth-child(5)').text();
									var _progress = $(this).find('td:nth-child(8)').text();
									console.log(_status);
									console.log(_progress);
									if (!(_status === 'completed') || !(_progress === 'billing_team')) {
										$(this).hide();
									} else {
										$(this).show();
									}
								});
							}


							$('.collection_team_button').on('click', function() {
								// Loop through each table row
								$('#completed-table tbody tr').each(function() {
									var _status = $(this).find('td:nth-child(5)').text();
									var _progress = $(this).find('td:nth-child(8)').text();
									console.log(_status);
									if (!(_status === 'completed') || !(_progress === 'collection_team')) {
										$(this).hide();
									} else {
										$(this).show();
									}
								});
							});

							// Event listener for Ready treasury team button
							$('.treasury_team_button').on('click', function() {
								// Loop through each table row
								$('#completed-table tbody tr').each(function() {
									var _status = $(this).find('td:nth-child(5)').text();
									var _progress = $(this).find('td:nth-child(8)').text();
									console.log(_status);
									if (!(_status === 'completed') || !(_progress === 'treasury_team')) {
										$(this).hide();
									} else {
										$(this).show();
									}
								});
							});
							$('.completed_team').on('click', function() {
								// Loop through each table row
								$('#completed-table tbody tr').each(function() {
									var _status = $(this).find('td:nth-child(5)').text();
									var _progress = $(this).find('td:nth-child(8)').text();
									console.log(_status);
									if (!(_status === 'completed') || !(_progress === 'completed')) {
										$(this).hide();
									} else {
										$(this).show();
									}
								});
							});


						});
					} else {
						// Show error message with SweetAlert
						Swal.fire({
							icon: 'error',
							title: 'Error',
							text: 'There was an error while saving the invoice: ' + xhr.statusText,
						});
					}
				});

			} else {
				// Show an error message
				console.log("Error updating ticket");
			}
		};
		xhr.send("progress=billing_team&status=completed");
	}


});


//Button for ongoing 
$(document).ready(function() {

	$('.ongoingBtn').on('click', function() {

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
		var userID = $(this).closest('tr').find('td:eq(13)').text();

		var toBilling = "billing_team";
		var toSupport = "support_team";

		console.log(status);
		console.log(progress);
		$('#thumbnail').attr('src', '/img/' + signature);


		if (progress === "support_team" || progress === "billing_team" || progress === "collection_team" || progress === "treasury_team" || progress === "completed") {

			if (status === "completed") {

				if (progress === "billing_team") {
					$('#proceed_billing').hide();
					$('#proceed_collection').show();
					$('#proceed_treasury').hide();
					$('#invoice_modal_details').hide();
					$('#send_to_collection').hide();
					$('#proof_confirmation_treasury').hide();
					$('#generateOR').hide();
				}
				else
					if (progress === "collection_team") {
						$('#send_to_collection').hide();
						$('#proceed_collection').hide();
						$('#proceed_treasury').show();
						$('#proceed_billing').hide();
						$('#proof_confirmation_treasury').hide();
						$('#invoice_modal_details').show();
						$('#generateOR').hide();
					}
					else
						if (progress === "treasury_team") {
							$('#send_to_collection').show();
							$('#proceed_collection').hide();
							$('#invoice_modal_details').show();
							$('#proof_confirmation_treasury').show();
							$('#proceed_billing').hide();
							$('#proceed_treasury').hide();
							$('#generateOR').hide();
						} else
							if (progress === "completed") {
								$('#generateOR').show();
								$('#send_to_collection').hide();
								$('#proceed_collection').hide();
								$('#invoice_modal_details').show();
								$('#proceed_billing').hide();
								$('#proceed_treasury').hide();
								$('#proceed_collection').hide();
								$('#proof_confirmation_treasury').hide();

							}
							else {
								$('#generateOR').hide();
								$('#proof_confirmation_treasury').hide();
								$('#invoice_modal_details').hide();
								$('#proceed_collection').hide();
								$('#progress_ongoing').val(toBilling);
								$('#proceed_billing').show();
								$('#span_warning').hide();
								$('#proceed_treasury').hide();
							}

			}
			else {
				$('#generateOR').hide();
				$('#invoice_modal_details').hide();
				$('#proceed_treasury').hide();
				$('#proceed_collection').hide();
				$('#proceed_billing').hide();
				$('#span_warning').show();

			}

			$('#ticket_status').show();
			$('#proceed_support').hide();
			// Add the justify-content-between class
			$('#buttonDiv').addClass('justify-content-between');
		}
		else {
			console.log(progress);
			$('#span_warning').hide();
			$('#progress_ongoing').val(toSupport);
			$('#proceed_billing').hide();

			$('#ticket_status').hide();
			// Remove the justify-content-between class
			$('#buttonDiv').removeClass('justify-content-between');

		}

		if (client_signature && client_payment_proof) {
			//images 

			//so that when the admin will click the button it will rewrite the data 


			$('#waiting_payment').hide();
			$('#waiting_signature').hide();
			$('#client_payment_proof').show();
			$('#client_signature').show();

			$('#span_warning_support').hide();


			$('#client_payment_proof').attr('src', '/img/' + client_payment_proof);
			$('#client_signature').attr('src', '/img/' + client_signature);

			$('#client_payment_proofs').val(client_payment_proof);
			$('#client_signatures').val(client_signature);
			$('#salesSignatures').val(signature);


			//this is just to check if progress is not going to billing or to support team
			$('#proceed_support').attr({
				"disabled": false
			});

		} else {


			$('#waiting_signature').show();
			$('#client_payment_proof').hide();
			$('#client_signature').hide();
			$('#span_warning_support').show();
			$('#proceed_support').attr({
				"disabled": true
			});
		}


		$('#user_id').val(userID);
		$('#ticket_id').val(ticketId);
		$('#conforme_date_ongoing').val(conforme_date_ongoing);
		$('#title_ongoing').val(title);
		$('#description_ongoing').val(description);
		$('#user_name_ongoing').val(userName);
		$('#status__').val(status);
		$('#amount_ongoing').val(amount);
		$('#conforme_no_ongoing').val(conformeNo); // set the conforme_no value in the #modalView modal
		$('#ongoingModal').modal();
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


/*	MENU FOR COMPLETED BUTTON*/



// Event listener for On-going Tickets button
$('.menu-item[data-table="completed"]').on('click', function() {
	// Show all rows
	$('#completed-table tbody tr').show();

	// Remove filters from other buttons
	$('.sales_team_button').removeClass('active');
	$('.billing_team_button').removeClass('active');
	$('.collection_team_button').removeClass('active');
	$('.treasury_team_button').removeClass('active');
});

// Event listener for Ready sales_team button
$('.sales_team_button').on('click', function() {
	// Loop through each table row
	$('#completed-table tbody tr').each(function() {
		var _status = $(this).find('td:nth-child(5)').text();
		var _progress = $(this).find('td:nth-child(8)').text();
		console.log(_status);

		if (!_status === 'completed' || !(_progress === 'support_team')) {
			$(this).hide();
		} else {
			$(this).show();
		}
	});
});

// Event listener for Ready billing team button
$('.billing_team_button').on('click', function() {
	// Loop through each table row
	applyBillingFilter();
});

function applyBillingFilter() {
	// Loop through each table row
	$('#completed-table tbody tr').each(function() {
		var _status = $(this).find('td:nth-child(5)').text();
		var _progress = $(this).find('td:nth-child(8)').text();
		console.log(_status);
		console.log(_progress);
		if (!(_status === 'completed') || !(_progress === 'billing_team')) {
			$(this).hide();
		} else {
			$(this).show();
		}
	});
}



$('.collection_team_button').on('click', function() {
	// Loop through each table row
	$('#completed-table tbody tr').each(function() {
		var _status = $(this).find('td:nth-child(5)').text();
		var _progress = $(this).find('td:nth-child(8)').text();
		console.log(_status);
		if (!(_status === 'completed') || !(_progress === 'collection_team')) {
			$(this).hide();
		} else {
			$(this).show();
		}
	});
});

// Event listener for Ready treasury team button
$('.treasury_team_button').on('click', function() {
	// Loop through each table row
	$('#completed-table tbody tr').each(function() {
		var _status = $(this).find('td:nth-child(5)').text();
		var _progress = $(this).find('td:nth-child(8)').text();
		console.log(_status);
		if (!(_status === 'completed') || !(_progress === 'treasury_team')) {
			$(this).hide();
		} else {
			$(this).show();
		}
	});
});


$('.completed_team').on('click', function() {
	// Loop through each table row
	$('#completed-table tbody tr').each(function() {
		var _status = $(this).find('td:nth-child(5)').text();
		var _progress = $(this).find('td:nth-child(8)').text();
		console.log(_status);
		if (!(_status === 'completed') || !(_progress === 'completed')) {
			$(this).hide();
		} else {
			$(this).show();
		}
	});
});



/*CONFORME BUTTON*/



// Add click event listener to the button in the first modal
$(document).ready(function() {
	$('.button_conforme').on('click', function(event) {
		// Extract data from the input fields in the second modal
		var ticketId = $('#id').val();
		var dateCreated = $('#date').val();
		var title = $('#title').val();
		var description = $('#description').val();
		var userName = $('#name').val();
		var conforme_no = $('#conforme_no').val();
		var currentDate = new Date().toLocaleDateString('en-US');
		// Pass the data to the corresponding input fields in the third modal
		$('#create_conforme input[name="ticket_id"]').val(ticketId);
		$('#create_conforme input[name="created_on"]').val(dateCreated);
		$('#create_conforme input[name="title"]').val(title);
		$('#create_conforme textarea[name="description"]').val(description);
		$('#create_conforme input[name="user_name"]').val(userName);
		$('#create_conforme input[name="status"]').val("ongoing");
		$('#create_conforme input[name="progress"]').val("sales_team");
		$('#create_conforme input[name="conforme_no"]').val(conforme_no);
		$('#create_conforme input[name="conforme_date"]').val(currentDate);
		// Show the third modal

		$('#create_conforme').modal();
	});
});

//ONGOING MODAL
$(document).ready(function() {
	$('.eBtn').on('click', function(event) {
		var ticketId = $(this).closest('tr').find('td:eq(0)').text();
		var dateCreated = $(this).closest('tr').find('td:eq(1)').text();
		var title = $(this).closest('tr').find('td:eq(2)').text();
		var description = $(this).closest('tr').find('td:eq(3)').text();
		var userName = $(this).closest('tr').find('td:eq(4)').text();
		var status = $(this).closest('tr').find('td:eq(5)').text();
		var conformeNo = $(this).closest('tr').find('td:eq(6)').text();
		$('#id').val(ticketId);
		$('#date').val(dateCreated);
		$('#title').val(title);
		$('#description').val(description);
		$('#name').val(userName);
		$('#status').val(status);
		$('#conforme_no').val(conformeNo);
		$('#modalView').modal();
	});
});


//ACCOUTN MODAL
$('.account_modal').on('click', function(event) {
	// Extract data from the input fields in the second modal
	var _userid = $(this).closest('tr').find('td:eq(0)').text();
	var _username = $(this).closest('tr').find('td:eq(1)').text();

	var _useremail = $(this).closest('tr').find('td:eq(2)').text();
	var _role = $(this).closest('tr').find('td:eq(3)').text();
	var _status = $(this).closest('tr').find('td:eq(4)').text();
	var password = $(this).closest('tr').find('td:eq(5)').text();
	$('#user_id_').val(_userid);
	$('#user_name_').val(_username);
	$('#user_email_').val(_useremail);
	$('#role').val(_role);
	$('#user_status').val(_status);
	$('#user_password_').val(password);
	$('#accountModal').modal();

});



$(document).ready(function() {
	$('.updateAccount').on('click', function() {
		// Extract data from the input fields in the second modal
		/*  var imageUpload = $('#image_upload').prop('files')[0];*/
		event.preventDefault();
		var selectElement = document.getElementById("dropdownRoleRoles");
		var selectedValue = selectElement.options[selectElement.selectedIndex].value;

		var formData = new FormData();
		formData.append("selectedValue", selectedValue);
		var userID = $("#user_id_").val();

		console.log(userID);

		var tableIdOngoing = "account-table";
		// Show loading animation with Swal
		Swal.fire({
			title: 'Loading . . .',
			allowEscapeKey: false,
			allowOutsideClick: false,
			didOpen: () => {
				Swal.showLoading();
			}
		});

		var xhr = new XMLHttpRequest();
		xhr.open('PUT', '/user/update/' + userID);
		xhr.onload = function() {
			if (xhr.status === 200) {
				// Show a success message


				Swal.fire({
					icon: 'success',
					title: 'Status Updated',
					text: 'User status is successfully updated!',
				}).then((result) => {
					if (result.isConfirmed) {
						$('#accountModal').modal('hide');

						$('#' + tableIdOngoing).load('/getUserUpdate?tableId=' + tableIdOngoing + ' #' + tableIdOngoing + ' > *', function() {
							$('.account_modal').on('click', function(event) {
								// Extract data from the input fields in the second modal
								var _userid = $(this).closest('tr').find('td:eq(0)').text();
								var _username = $(this).closest('tr').find('td:eq(1)').text();

								var _useremail = $(this).closest('tr').find('td:eq(2)').text();
								var _role = $(this).closest('tr').find('td:eq(3)').text();
								var _status = $(this).closest('tr').find('td:eq(4)').text();
								var password = $(this).closest('tr').find('td:eq(5)').text();
								$('#user_id_').val(_userid);
								$('#user_name_').val(_username);
								$('#user_email_').val(_useremail);
								$('#role').val(_role);
								$('#user_status').val(_status);
								$('#user_password_').val(password);
								$('#accountModal').modal();

							});

						});

					} else {
						// Show error message with SweetAlert
						Swal.fire({
							icon: 'error',
							title: 'Error',
							text: 'There was an error while saving the user data; ' + xhr.statusText,
						});
					}
				});

			} else {
				// Show an error message
				console.log("Error Updating Ticket");
			}
		};
		xhr.send(formData);
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

	// Check if active class is set to profile-table and hide create ticket button
	if ($(menuItem).hasClass('active') && selectedTableId === "profile") {
		$('.btn.btn-primary').hide();
		$('#tickets_lists').hide();
		$('#dashboard_header').addClass('profile-table').hide();
	} else {
		$('#tickets_lists').show();
		$('#dashboard_header').removeClass('profile-table').show();
		$('.btn.btn-primary').show();
	}
}