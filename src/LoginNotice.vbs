
result = msgbox("현재 로그인이 풀려있어요."+vbCrLf+"(처음인 경우엔 정상이에요)"+vbCrLf+"로그인하신 후 아래 확인 버튼을 누르세요."+vbCrLf+vbCrLf+"주의 : 로그인 전에 확인을 누르거나 창을 닫으면 다음번 프로그램 실행 시에 출석체크가 진행됩니다.", vbOkOnly,"Genshin Auto Check-in")

If result = 1 Then
set objFS = CreateObject("Scripting.FileSystemObject")
' Create done.txt to notice to GADC that the user has done his login. Also, perform a role of as the mark of initial execution.
objFS.CreateTextFile(".\done.txt")
End If