from flask import Blueprint, render_template, redirect, url_for, flash, request
from flask_login import login_user, logout_user, current_user
from urllib.parse import urlparse
from app import db
from app.models import User

bp = Blueprint('auth', __name__)

@bp.route('/register', methods=['GET', 'POST'])
def register():
    if current_user.is_authenticated:
        return redirect(url_for('main.index'))
    
    if request.method == 'POST':
        username = request.form.get('username')
        email = request.form.get('email')
        password = request.form.get('password')
        
        if not username or not email or not password:
            flash('Todos os campos são obrigatórios', 'error')
            return render_template('register.html')
        
        if User.query.filter_by(username=username).first():
            flash('Nome de usuário já existe', 'error')
            return render_template('register.html')
        
        if User.query.filter_by(email=email).first():
            flash('Email já cadastrado', 'error')
            return render_template('register.html')
        
        user = User(username=username, email=email)
        user.set_password(password)
        db.session.add(user)
        db.session.commit()
        
        flash('Cadastro realizado com sucesso!', 'success')
        return redirect(url_for('auth.login'))
    
    return render_template('register.html')

@bp.route('/login', methods=['GET', 'POST'])
def login():
    if current_user.is_authenticated:
        return redirect(url_for('main.index'))
    
    if request.method == 'POST':
        username = request.form.get('username')
        password = request.form.get('password')
        
        user = User.query.filter_by(username=username).first()
        
        if user and user.check_password(password):
            login_user(user)
            next_page = request.args.get('next')
            # Validate next_page is a safe relative URL
            if next_page:
                parsed = urlparse(next_page)
                # Only allow relative URLs (no scheme, no netloc)
                if parsed.netloc or parsed.scheme:
                    next_page = None
            return redirect(next_page if next_page else url_for('main.index'))
        
        flash('Usuário ou senha inválidos', 'error')
    
    return render_template('login.html')

@bp.route('/logout')
def logout():
    logout_user()
    return redirect(url_for('auth.login'))
