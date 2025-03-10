document.addEventListener('DOMContentLoaded', function() {
  var smallCalendarEl = document.getElementById('calendar');
  var fullCalendarEl = document.getElementById('full-calendar');

  var smallCalendar = new FullCalendar.Calendar(smallCalendarEl, {
    initialView: 'dayGridMonth',
    dateClick: function(info) {
      showLargeCalendar(info.dateStr);
    }
  });

  smallCalendar.render();

  function showLargeCalendar(dateStr) {
    document.getElementById('small-calendar').style.display = 'none';
    document.getElementById('large-calendar').style.display = 'block';

    var largeCalendar = new FullCalendar.Calendar(fullCalendarEl, {
      initialView: 'dayGridMonth',
      initialDate: dateStr
    });

    largeCalendar.render();
  }
});
