package naukri.engine

class Transform : Component() {
    // 注意 z 軸是一個虛擬軸，並不參與物理判斷
    var position = Vector3(0F, 0F, 0F)

    var scale = Vector2(1F, 1F)
}