<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<!DOCTYPE html>
<html>

<head>
  <meta charset="UTF-8">
  <meta http-equiv="X-UA-Compatible" content="IE=edge">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>My - Schedule</title>
  <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.0.2/dist/css/bootstrap.min.css" rel="stylesheet"
    integrity="sha384-EVSTQN3/azprG1Anm3QDgpJLIm9Nao0Yz1ztcQTwFspd3yD65VohhpuuCOmLASjC" crossorigin="anonymous">
  <link rel="stylesheet" href="./src/calendar-gc.css">
  <style>
    html,
    body {
      margin: 0;
      overflow-x: hidden;
    }
    #popup01{
    	display: none;
	}
	#popup01{
		width: 500px;
		height: 500px;
		position: absolute;
		top: 50%;
		left: 50%;
		margin: -250px 0 0 -250px;
		background-color: #fff;
		z-index: 2;
		border: 2px solid #000000;
	}
	.backon{
	    content: "";
	    width: 100%;
	    height: 100%;
	    background: #00000054;
	    position: fixed;
	    top: 0;
	    left: 0;
	    z-index: 1;
	}
	.close{
	  position:absolute;
	  top:-25px;
	  right: 0;
	  cursor:pointer;
	  background-color:white;
	  padding: 0 5px 0 5px;
	  border: 2px solid #000000;
	}
	
	.openPopup{
	
	  cursor:pointer;
	
	}
  </style>
</head>

<body>
<input type="hidden" id="preTitle" />
<input type="hidden" id="preContents" />
<input type="hidden" id="schedule_id" />
  <div id="calendar" style="padding: 1rem;"></div>
<!--   <div class="openPopup">클릭하면 팝업이 나와요</div>    -->
	<div id="popup01">
	    <div class="close">close</div>
	    <div style="margin-left:20px;margin-top:20px;">
		    <div>
			    <div style="font-weight:bold">제목</div>
			    <input id="title" type="text" style="width:200px;" />
			    <input id="clickDate" type="hidden" />
			    <input id="scheduleDate" type="hidden" />
		    </div>
		    <div style="height:10px;"></div>
		    <div>
			    <div style="font-weight:bold">내용</div>
			    <textarea rows="" cols="" id="content" style="width:200px;"></textarea>
		    </div>
		    <div style="height:10px;"></div>
		    <div>
			    <button id="save">저장</button>
			    <button id="delete">삭제</button>
		    </div>
		    <div style="height:10px;"></div>
		    <div>※특수기호(,)는 사용할 수 없습니다.</div>
	    </div>
	</div>
</body>
<script src="https://code.jquery.com/jquery-3.6.0.min.js"
  integrity="sha256-/xUj+3OJU5yExlq6GSYGSHk7tPXikynS7ogEvDej/m4=" crossorigin="anonymous"></script>
<script src="./src/calendar-gc.js"></script>
<script>
//var clickDate = "";
var list = "${list}";
console.log("list ",list);
list = list.replace("[{","");
list = list.replace("}]","");

var listArray = list.split("}, {");

//string데이터 배열과 오브젝트를 이용하여 리스트(오브젝트)로 변경
var listArrayFinal = [];
var listObj = {};

for( var i = 0 ; i < listArray.length ; i++ ) {
	listObj.reg_date = 		(listArray[i].split(", ")[0]).split("=")[1];
	listObj.contents = 		(listArray[i].split(", ")[1]).split("=")[1];
	listObj.id = 			(listArray[i].split(", ")[2]).split("=")[1];
	listObj.title = 		(listArray[i].split(", ")[3]).split("=")[1];
	listObj.schedule_date = (listArray[i].split(", ")[4]).split("=")[1];
	listArrayFinal.push(listObj);
	listObj = {};
}
console.log("listArrayFinal: ", listArrayFinal);
  $(function (e) {
	
	//init();
    var calendar = $("#calendar").calendarGC({
      dayBegin: 0,
      prevIcon: '&#x3c;',
      nextIcon: '&#x3e;',
      onPrevMonth: function (e) {
        console.log("prev");
        console.log(e);
        
      },
      onNextMonth: function (e) {
        console.log("next");
        console.log(e);
        
      },
      events: getHoliday(),
      onclickDate: function (e, data) {
        //console.log(e, data.date);
        var clickDate = data.datejs;
        var year 	= clickDate.getFullYear();
        var month 	= clickDate.getMonth()+1 >= 10 ? clickDate.getMonth()+1 : '0' + (clickDate.getMonth()+1);
        var date 	= clickDate.getDate() >= 10 ? clickDate.getDate() : '0' + clickDate.getDate();
        console.log("date: ", year+month+date);
        $("#clickDate").val(clickDate.getDate().toString());
        $("#scheduleDate").val(year+month+date);
        console.log("clickDate: ", $("#clickDate").val());
        console.log("schedule_date: ", $("#scheduleDate").val());
        //처음 저장시 예전데이터는 존재하지 않으므로 비워준다
        $("#preTitle").val("");
  	  	$("#preContents").val("");
  	  	$("#schedule_id").val("");
  	  	$("#delete").css("display", "none");
  	  	//팝업오픈
        $("#popup01").show();
  	  	//뒷배경
        $("body").append('<div class="backon"></div>');
      }
    });
    init();
    
    $("body").on("click", function(event) { 
        if(event.target.className == 'close' || event.target.className == 'backon'){
        	$("#title").val('');
           	$("#content").val('');
           	$("#clickDate").val('');
           	$("#scheduleDate").val('');
            $("#popup01").hide(); //close버튼 이거나 뒷배경 클릭시 팝업 삭제
            $(".backon").hide();
        }
    });
    //저장,수정
    $("#save").on("click", function(event) { 
        //저장로직 - ajax -> db -> success
        var title = $("#title").val();
        var content = $("#content").val();
        var clickDate = $("#clickDate").val();
        var schedule_date = $("#scheduleDate").val();
        var schedule_id = $("#schedule_id").val();
        
        //이전데이터와 비교하여 수정사항이 없다면
        if ( title == $("#preTitle").val() && content == $("#preContents").val() ) {
			alert("변경된 항목이 없습니다.")
			return false;
        }
        
        console.log("clickDate: ", clickDate);
        var param = {"title" : title, "content" : content, "date" : clickDate, "schedule_date" : schedule_date, "schedule_id" : schedule_id};
        
        if ( schedule_id == null || schedule_id == undefined || schedule_id =='' ) {
	        var result = confirm("저장하시겠습니까?");
        } else {
        	var result = confirm("수정하시겠습니까?");
        }
        if ( result == true ) {
	        $.ajax({
				timeout: 10000, // 시간 제한
				type:"POST", // 전송 방식 GET , POST , PUT , DELETE
				contentType:'application/json; charset=utf-8',
				url:"/schedule/save.do", // 전송할 경로
				data: JSON.stringify(param) , // 전송할 키와 값
				dataType:"JSON", // 반환되는 값의 형식 json , text 를 주로 사용
			
				success : function(data) {
				// 작업이 성공했을 경우 json 형태로 받는다.
		            if ( typeof(data) == "undefined" ) {return;} // 개체 확인
		            else {
		            	$("#title").val('');
		               	$("#content").val('');
		               	$("#clickDate").val('');
						console.log("result: ", data);
						//setSchedule(data);
						if ( data.result !== null && data.result !== undefined && data.result !== 0 ) {
							alert("저장되었습니다.");
							$("#popup01").hide(); //close버튼 이거나 뒷배경 클릭시 팝업 삭제
			                $(".backon").hide();
							//location.reload();
			                setSchedule(data.scheduleList);
						}
		            }
	
				},
				
				error : function(request, status, errorr) {
					// 에러가 났을 경우의 작업
					alert(status);
				}
			}); // end ajax
        } else {
        	return false;
        }
    });
    //삭제
    $("#delete").on("click", function(event) { 
        //저장로직 - ajax -> db -> success
       
        var schedule_id = $("#schedule_id").val();
        var param = {"schedule_id" : schedule_id};
        var result = confirm("스케줄을 삭제하시겠습니까?");
        if ( result == true ) {
        	$.ajax({
    			timeout: 10000, // 시간 제한
    			type:"POST", // 전송 방식 GET , POST , PUT , DELETE
    			contentType:'application/json; charset=utf-8',
    			url:"/schedule/delete.do", // 전송할 경로
    			data: JSON.stringify(param) , // 전송할 키와 값
    			dataType:"JSON", // 반환되는 값의 형식 json , text 를 주로 사용
    		
    			success : function(data) {
    			// 작업이 성공했을 경우 json 형태로 받는다.
    	            if ( typeof(data) == "undefined" ) {return;} // 개체 확인
    	            else {
    	            	$("#title").val('');
    	               	$("#content").val('');
    	               	$("#clickDate").val('');
    					console.log("result: ", data);
    					if ( data.result > 0 ) {
    						alert("스케줄이 삭제되었습니다.");					
	    	               	$("#popup01").hide(); //close버튼 이거나 뒷배경 클릭시 팝업 삭제
	    	                $(".backon").hide();
	    					
	    	                setSchedule(data.scheduleList);
    					}
    	            }

    			},
    			
    			error : function(request, status, errorr) {
    				// 에러가 났을 경우의 작업
    				alert(status);
    			}
    		}); // end ajax
        } else {
        	return false;
        }
        
    });
  });
  
  function callScheduleList() {
	  var param = {};
  		
	  $.ajax({
		timeout: 10000, // 시간 제한
		type:"POST", // 전송 방식 GET , POST , PUT , DELETE
		contentType:'application/json; charset=utf-8',
		url:"/schedule/selectList.do", // 전송할 경로
		data: JSON.stringify(param) , // 전송할 키와 값
		dataType:"JSON", // 반환되는 값의 형식 json , text 를 주로 사용
	
		success : function(data) {
		// 작업이 성공했을 경우 json 형태로 받는다.
            if ( typeof(data) == "undefined" ) {return;} // 개체 확인
            else {
            	console.log("data ", data);
            	setSchedule(data.scheduleList);
            }

		},
		
		error : function(request, status, errorr) {
			// 에러가 났을 경우의 작업
			alert(status);
		}
	}); // end ajax
  }
  
  function init() {
  	//setSchedule();
	    //초기 스케줄 셋
	    $("td.current-month").each(function(index, item) {
			 //console.log($(item).find("span").text());
			 $(listArrayFinal).each(function(id, obj) {
				 if ( $(item).find("input[type=hidden]").val() == obj.schedule_date ) {
					 console.log("Y");
					 $(item).append('<div class="gc-evnet badge bg-primary" onclick="editSchedule(\'' + obj.title + 
							 '\', \''+ obj.contents + '\', \'' + obj.schedule_date.substr(6,2).replace("0","") + '\', \'' + 
							 obj.id +'\', \'' + obj.schedule_date + '\')">' + 
							 obj.title + '</div>');
				 }
			 })
		});
  }
  function setSchedule(dataList) {

	  $("td.current-month").each(function(index, item) {
		  $(item).find("div").remove();
	  });
	  $("td.current-month").each(function(index, item) {
			 //console.log($(item).find("span").text());
			 $(dataList).each(function(id, obj) {
				 if ( $(item).find("input[type=hidden]").val() == obj.schedule_date ) {
					 console.log("Y");
					 $(item).append('<div class="gc-evnet badge bg-primary" onclick="editSchedule(\'' + obj.title + 
							 '\', \''+ obj.contents + '\', \'' + obj.schedule_date.substr(6,2).replace("0","") + '\', \'' + 
							 obj.id +'\', \'' + obj.schedule_date + '\')">' + 
							 obj.title + '</div>');
				 }
			 })
		});
  }
  function editSchedule(title, content, date, id, realDate) {
	  $("#delete").css("display", "inline");
	  $("#title").val(title);
	  $("#content").val(content);
	  //미리 저장된 데이터이므로 예전데이터에 데이터 입력
	  $("#preTitle").val($("#title").val());
	  $("#preContents").val($("#contents").val());
	  $("#schedule_id").val(id);
	  $("#clickDate").val(date);
	  $("#scheduleDate").val(realDate);
	  //팝업오픈
	  $("#popup01").show();   //팝업 오픈
	  //뒷배경 생성
      $("body").append('<div class="backon"></div>'); //뒷배경 생성
  }
  function getHoliday() {
    var d = new Date();
    var totalDay = new Date(d.getFullYear(), d.getMonth(), 0).getDate();
    var events = [];

    for (var i = 1; i <= totalDay; i++) {
      var newDate = new Date(d.getFullYear(), d.getMonth(), i);
//       if (newDate.getDay() == 0) {   //if Sunday
//         events.push({
//           date: newDate,
//           eventName: "Sunday free",
//           className: "badge bg-secondary",
//           onclick(e, data) {
//             console.log(data);
//           },
//           dateColor: "red"
//         });
//       }
//       if (newDate.getDay() == 6) {   //if Saturday
//         events.push({
//           date: newDate,
//           eventName: "Saturday free",
//           className: "badge bg-danger",
//           onclick(e, data) {
//             console.log(data);
//           },
//           dateColor: "red"
//         });
//       }

    }
    return events;
  }

  getHoliday()
</script>

</html>