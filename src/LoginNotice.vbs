
result = msgbox("���� �α����� Ǯ���־��."+vbCrLf+"(ó���� ��쿣 �����̿���)"+vbCrLf+"�α����Ͻ� �� �Ʒ� Ȯ�� ��ư�� ��������."+vbCrLf+vbCrLf+"���� : �α��� ���� Ȯ���� �����ų� â�� ������ ������ ���α׷� ���� �ÿ� �⼮üũ�� ����˴ϴ�.", vbOkOnly,"Genshin Auto Check-in")

If result = 1 Then
set objFS = CreateObject("Scripting.FileSystemObject")
' Create done.txt to notice to GADC that the user has done his login. Also, perform a role of as the mark of initial execution.
objFS.CreateTextFile(".\done.txt")
End If