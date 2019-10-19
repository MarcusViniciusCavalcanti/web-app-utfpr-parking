// message
class MessageSuccess {

    constructor(type, lastAccess, lastHoursAccess, carModel, plate, tax, accessNumber, userId, userName, authorizedAccess) {
        this._name = name;
        this._type = type;
        this._lastAccess = lastAccess;
        this._lastHoursAccess = lastHoursAccess;
        this._carModel = carModel;
        this._plate = plate;
        this._tax = tax;
        this._accessNumber = accessNumber;
        this._userName = userName;
        this._authorizedAccess = authorizedAccess;
        this._id = userId;
    }

    get name() {
        return this._userName;
    }

    get userId() {
        return this._id;
    }

    get type() {
        return this._type;
    }

    get lastAccess() {
        return this._lastAccess;
    }

    get lastHoursAccess() {
        return this._lastHoursAccess;
    }

    get carModel() {
        return this._carModel;
    }

    get plate() {
        return this._plate;
    }

    get tax() {
        return this._tax;
    }

    get accessNumber() {
        return this._accessNumber;
    }

    get isAuthorizedAccess() {
        return this._authorizedAccess;
    }

}

class View {
    constructor() {
        this._recognizeSection = $('#recognizers');
    }

    template(message) {
        throw new Error('Você precisa implementar o método template')
    }

    update(message) {
        this._recognizeSection.children('div').remove();
        const div = document.createElement('div');
        div.innerHTML = `
            <div class="col-sm-4 col-md-offset-2">
              <div class="user-info-sidebar">
                <a href="#" class="user-img">
                  <img src="/user/avatar/${message.userId}" alt="user-img" class="img-cirlce img-responsive img-thumbnail" />
                </a>
                
                <a href="/users/${message.userId}">
                  <span class="user-status is-online"></span>
                </a>
                
                <span class="user-title"> ${message.name}</span>
                
                <hr />
                
                <ul class="list-unstyled user-info-list">
                  <li>
                    <i class="fa-automobile"></i> ${message.carModel}
                  </li>
                  ${this.checkType(message.type)}
                  <li>
                    <i class="fa-camera-retro"></i> Placa reconhecida ${message.plate}
                  </li>
                </ul>
                
                <hr />
                
                <ul>
                  <li><span>Acessos: </span>${message.accessNumber}</li>
                </ul>
              </div>
            </div>
                
            ${this.template(message)}
        `;

        this._recognizeSection.append(div);

        animationSidebar();
        animationNotification();
        animationTax();
    }

    checkType(type) {
        if (type === 'Servidor' || type === 'Operador') {
            return `
                  <li>
                    <i class="fa-briefcase"></i> Servidor
                  </li>
            `;
        } else if (type === 'Aluno') {
            return `
                  <li>
                    <i class="fa-graduation-cap"></i> Aluno
                  </li>
            `;
        } else if (type === 'Visitante') {
            return `
                  <li>
                    <i class="fa-slideshare"></i> Palestrante
                  </li>
            `;
        } else {
            return `
                  <li>
                    <i class="fa-question"></i>Desconhecido
                  </li>
            `;
        }
    }
}

class ViewAuthorize extends View {
    template(message) {
        return `
            <div class="col-sm-4">
              <div class="xe-widget xe-progress-counter xe-progress-counter-green" data-count=".num" data-from="0" data-to="${message.tax}" data-duration="2">
                <div class="xe-background" style="left: -25%">
                  <i class="linecons-thumbs-up"></i>
                </div>
                
                <div class="xe-upper">
                  <div class="xe-icon">
                    <i class="linecons-thumbs-up"></i>
                  </div>
                  <div class="xe-label">
                    <h3 class="text-center">Acesso Autorizado</h3>
                    <p class="bg-success text-center">Usuário reconhecido</p>
                    <span>Taxa de acerto</span>
                    <strong class="num">0</strong>
                  </div>
                </div>
                
                <div class="xe-progress">
                  <span class="xe-progress-fill" data-fill-from="0" data-fill-to="${message.tax}" data-fill-unit="%" data-fill-property="width" data-fill-duration="2" data-fill-easing="true"></span>
                </div>

                <div class="xe-lower">
                  <span>Último acesso</span>
                    <strong>${message.lastAccess} as ${message.lastHoursAccess}</strong>
                </div>
              </div>
            </div>
        `;
    }
}

class ViewUnauthorize extends View {
    template(message) {
        return `
            <div class="col-sm-4">
              <div class="xe-widget xe-progress-counter xe-progress-counter-pink" data-count=".num" data-from="0" data-to="${message.tax}" data-duration="2">
                <div class="xe-background" style="left: -25%">
                  <i class="linecons-lock"></i>
                </div>
                
                <div class="xe-upper">
                  <div class="xe-icon">
                    <i class="linecons-lock"></i>
                  </div>
                  <div class="xe-label">
                    <h3 class="text-center">Acesso Não Autorizado</h3>
                    <p class="bg-danger text-center">Usuário reconhecido</p>
                    <span>Taxa de acerto</span>
                    <strong class="num">0</strong>
                  </div>
                </div>
                
                <div class="xe-progress">
                  <span class="xe-progress-fill" data-fill-from="0" data-fill-to="${message.tax}" data-fill-unit="%" data-fill-property="width" data-fill-duration="2" data-fill-easing="true"></span>
                </div>

                <div class="xe-lower">
                  <span>Último acesso</span>
                    <strong>${message.lastAccess} as ${message.lastHoursAccess}</strong>
                </div>
              </div>
            </div>
        `;
    }
}

class RecognizeSuccess {

    set message(message) {
        this._message = message;
        this._authorize = new ViewAuthorize();
        this._unauthorize = new ViewUnauthorize();
    }

    render() {
        if(this._message.isAuthorizedAccess) {
            this._authorize.update(this._message)
        } else {
            this._unauthorize.update(this._message)
        }
    }
}

class RecognizerFail {
    constructor() {
        this._recognizeSection = $('#recognizers');
    }

    set message(message) {
        this._message = message;
    }

    render() {
        this._recognizeSection.children('div').remove();
        const div = document.createElement('div');
        div.innerHTML = `
        <div class="row">
          <div class="panel panel-default">
            <div class="panel-body">
              <div class="col-sm-6 col-md-offset-3">
                <div class="xe-widget xe-progress-counter xe-progress-counter-yellow" data-count=".num" data-from="0" data-to="${this._message.tax}" data-duration="2">

                  <div class="xe-background" style="left: -15%">
                    <i class="linecons-eye"></i>
                </div>

                <div class="xe-upper">
                  <div class="xe-icon">
                    <i class="linecons-eye"></i>
                  </div>
                  <div class="xe-label">
                    <h3 class="text-center">Acesso Não Autorizado</h3>
                    <p class="bg-warning text-center">
                      Usuário não encontrado na base de dados <br/>
                      Verique o pedido de acesso
                    </p>
                    <span>Taxa de acerto</span>
                    <strong class="num">0</strong>
                  </div>
                </div>

                <div class="xe-progress">
                  <span class="xe-progress-fill" data-fill-from="0" data-fill-to="${this._message.tax}" data-fill-unit="%" data-fill-property="width" data-fill-duration="2" data-fill-easing="true"></span>
                </div>

                <div class="xe-lower">
                  <span>Isso pode indicar que o usuário não foi cadastrado</span>
                  <strong>Verifique esta ocorrência</strong>
                </div>

              </div>
            </div>
          </div>
        </div>
        `;

        this._recognizeSection.append(div);
        animationTax();
        animNotficationFail();
    }
}

function animationSidebar() {
    const siderbarUser = document.querySelector('.user-info-sidebar');

    return TweenMax.from(siderbarUser, 1, {
        x: 100,
        opacity: 0,
        ease: Back.easeOut.config(1.7), x: -500,
    });


}

function animationNotification() {
    const notification = document.querySelector('.xe-widget');

    return TweenMax.from(notification, 1, {
        x: -100,
        opacity: 0,
        ease: Back.easeOut.config(1.7), x: 500,
    });
}

function animNotficationFail() {
    const notification = document.querySelector('.xe-widget');

    return TweenMax.from(notification, 1, {
        scale: 0,
        opacity: 0,
        ease: Back.easeOut.config(1.7), scale: 0, opacity: 0
    });
}

function animationTax() {
    $("[data-fill-from][data-fill-to]").each(function(i, el) {
        var $el = $(el),
            sm = scrollMonitor.create(el);

        sm.fullyEnterViewport(function()
        {
            var fill = {
                    current: 	null,
                    from: 		attrDefault($el, 'fill-from', 0),
                    to: 		attrDefault($el, 'fill-to', 100),
                    property: 	attrDefault($el, 'fill-property', 'width'),
                    unit: 		attrDefault($el, 'fill-unit', '%'),
                },
                opts 		= {
                    current: fill.to, onUpdate: function(){
                        $el.css(fill.property, fill.current + fill.unit);
                    },
                    delay: attrDefault($el, 'delay', 0),
                },
                easing 		= attrDefault($el, 'fill-easing', true),
                duration 	= attrDefault($el, 'fill-duration', 2.5);

            if(easing)
            {
                opts.ease = Sine.easeOut;
            }

            // Set starting point
            fill.current = fill.from;

            TweenMax.to(fill, duration, opts);

            sm.destroy();
        });
    });

    $("[data-from][data-to]").each(function(i, el) {
        var $el = $(el),
            sm = scrollMonitor.create(el);

        sm.fullyEnterViewport(function()
        {
            var opts = {
                    useEasing: 		attrDefault($el, 'easing', true),
                    useGrouping:	attrDefault($el, 'grouping', true),
                    separator: 		attrDefault($el, 'separator', ','),
                    decimal: 		attrDefault($el, 'decimal', '.'),
                    prefix: 		attrDefault($el, 'prefix', ''),
                    suffix:			attrDefault($el, 'suffix', ''),
                },
                $count		= attrDefault($el, 'count', 'this') == 'this' ? $el : $el.find($el.data('count')),
                from        = attrDefault($el, 'from', 0),
                to          = attrDefault($el, 'to', 100),
                duration    = attrDefault($el, 'duration', 2.5),
                delay       = attrDefault($el, 'delay', 0),
                decimals	= new String(to).match(/\.([0-9]+)/) ? new String(to).match(/\.([0-9]+)$/)[1].length : 0,
                counter 	= new countUp($count.get(0), from, to, decimals, duration, opts);

            setTimeout(function(){ counter.start(); }, delay * 1000);

            sm.destroy();
        });
    });
}

let stomp = null;
const recognizeSuccess = new RecognizeSuccess();
const recognizeFail = new RecognizerFail();

$(document).ready(function(){
    connect();
});

function connect(){
    const headerName = '${_csrf.headerName}';
    const token = '${_csrf.token}';
    const headers = {};
    headers[headerName] = token;

    const socket = new SockJS('/recognize');
    stomp = Stomp.over(socket);
    stomp.connect(headers, function(frame){
        stomp.subscribe('/app/recognize', function (messageOutput){
            console.log("INITIAL: "+messageOutput);
            const progressList = $.parseJSON(messageOutput.body);
            $.each(progressList,function(index, element){
                update(element);
            });
        });

        stomp.subscribe('/topic/recognize', function(messageOutput) {
            console.log("New Message: "+ messageOutput);
            const json = $.parseJSON(messageOutput.body);
            update(json);
        });
    });
}

function update(json) {
    if (json) {
        let viewRecognize = null;

        if (json.identified) {
            let messageSuccess = new MessageSuccess(
                json.driver.type,
                json.driver.lastAccess,
                json.driver.lastHoursAccess,
                json.driver.carModel,
                json.driver.plate,
                json.tax,
                json.driver.accessNumber,
                json.driver.userId,
                json.driver.userName,
                json.driver.authorizedAccess
            );
            recognizeSuccess.message = messageSuccess;
            viewRecognize = recognizeSuccess
        } else {
            recognizeFail.message = json;
            viewRecognize = recognizeFail;
        }

        viewRecognize.render();
    }
}
