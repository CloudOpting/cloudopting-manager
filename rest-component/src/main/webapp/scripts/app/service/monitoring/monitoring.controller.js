'use strict';

angular.module('cloudoptingApp')
    .controller('MonitoringController', function (SERVICE, $scope, $state, $log, localStorageService, InstanceService) {

        //We should get the information of the current instance.



        new Morris.Line({
            // ID of the element in which to draw the chart.
            element: 'myfirstchart',
            // Chart data records -- each entry in this array corresponds to a point on
            // the chart.
            data: [
                { time: '2001', disk: 20, cpu: 12, ram: 15 },
                { time: '2002', disk: 10, cpu: 5, ram: 13 },
                { time: '2003', disk: 5, cpu: 20, ram: 19 },
                { time: '2004', disk: 5, cpu: 3, ram: 0 },
                { time: '2005', disk: 20, cpu: 10, ram: 5 }
            ],
            // The name of the data record attribute that contains x-values.
            xkey: 'time',
            // A list of names of data record attributes that contain y-values.
            ykeys: ['disk', 'cpu', 'ram'],
            // Labels for the ykeys -- will be displayed when you hover over the
            // chart.
            labels: ['Disk', 'CPU', 'RAM'],
            lineColors: ['green', 'blue', 'orange']
        });
    }
);
