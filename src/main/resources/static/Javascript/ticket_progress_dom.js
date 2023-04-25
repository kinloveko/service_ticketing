/**
 * 
 */

//for status color
// get the span element
var statusSpan = document.querySelector('.ticket-status');


// check the status value and add the appropriate class
if (statusSpan.textContent === 'ongoing') {
	statusSpan.classList.add('on-going');


} else if (statusSpan.textContent === 'completed') {
	statusSpan.classList.add('closed');

};


//for completed button
//if the completed button is clicked it will change the status to completed 
function showConfirmation() {
	var ticketId = $('#span_value').text();

	Swal.fire({
		title: 'Are you sure?',
		text: "You won't be able to revert this!",
		icon: 'warning',
		showCancelButton: true,
		confirmButtonColor: '#3085d6',
		cancelButtonColor: '#d33',
		confirmButtonText: 'Yes, mark it as completed!'
	}).then((result) => {
		if (result.isConfirmed) {
			// Make an AJAX call to update the ticket
			$.ajax({
				type: "PUT",
				url: "/tickets/update-ticket/" + ticketId,
				data: {
					"progress": "support_team",
					"status": "completed"
				},
				success: function() {
					// Show a success message
					Swal.fire(
						'Marked as completed!',
						'The ticket has been marked as completed.',
						'success'
					).then((result) => {
						if (result.isConfirmed) {
							location.reload();
						}
					});
				},
				error: function() {
					// Show an error message
					Swal.fire(
						'Oops...',
						'Something went wrong!',
						'error'
					);
				}
			});
		}
	});
}
//for viewing all the feedbacks


$('.view_feedback_modal').on('click', function(event) {
	var currentDate = new Date().toLocaleString('en-US');

	var statusId = $('#status_id_').val();
	$('#status_id_feedback').val(statusId);
	$('#feedback_date').val(currentDate);
	$('#view_feedback_modal').modal();
	$('#view_progress').modal('hide');
	$('#span_id').text(statusId);


	$.ajax({
		type: "GET",
		url: "/feedbacks?statusId=" + statusId,
		success: function(feedbacks) {
			var messages = $('#_messages');
			messages.empty(); // clear existing content
			if (feedbacks.length === 0) {
				messages.append('<span style="color:red">No feedbacks yet.</span>');
			} else {
				$.each(feedbacks, function(index, feedback) {
					messages.append('<div class="feedback">' + '<p class="message">' + feedback.feedback_message + '</span>' +
						'<span class="feedback-time">' + feedback.feedback_date_time + '</span>' +
						'</div>');
				});
			}
		},
		error: function(jqXHR, textStatus, errorThrown) {
			// handle the error here
			console.log(textStatus);
		}
	});

});


//for adding a feedbacks
$('.feedback_modal').on('click', function(event) {

	var currentDate = new Date().toLocaleString('en-US');
	var status_id_ = $('#status_id_').val();

	$('#status_id_feedback_').val(status_id_);
	$('#feedback_date').val(currentDate);
	$('#add_feedback_modal').modal();
	$('#view_progress').modal('hide');
});


//adding a progress
$('.add_button_update').on('click', function(event) {

	var currentDate = new Date().toLocaleString('en-US');

	$('#status_date_time_add').val(currentDate);
	$('#status_progress_add').val("ongoing");

	$('#support_add_modal').modal();

});


//viewing the progress
$('.eBtn').on('click', function(event) {

	var status_id_ = $(this).closest('tr').find('td:eq(0)').text();
	var _title = $(this).closest('tr').find('td:eq(1)').text();
	var _comments = $(this).closest('tr').find('td:eq(2)').text();

	var dateCreated = $(this).closest('tr').find('td:eq(3)').text();
	var _progress = $(this).closest('tr').find('td:eq(4)').text();

	$('#status_id_').val(status_id_);
	$('#title').val(_title);
	$('#comments').val(_comments);
	$('#date').val(dateCreated);
	$('#status').val(_progress);

	$('#view_progress').modal();

});
